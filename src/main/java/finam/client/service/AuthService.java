package finam.client.service;

import finam.client.component.JwtTokenStore;
import finam.client.dto.AuthResponseDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Сервис авторизации, ответственный за управление процессом получения и обновления JWT-токенов.
 */
@AllArgsConstructor
@Service
@Slf4j
public class AuthService {

    private final Environment env;
    private final WebClient webClient;
    private final JwtTokenStore jwtTokenStore;

    private static final String SECRET_KEY_PROPERTY_NAME = "secret.key";

    /**
     * Получение JWT-токена методом POST-запроса к внешнему API.
     *
     * @param secretKey Секретный ключ для формирования тела запроса
     * @return Монадический объект с результатом авторизации (токеном и датой).
     */
    public Mono<AuthResponseDTO> getJwtToken(String secretKey) {
        try {
            String requestBody = "{\"secret\":\"" + secretKey + "\"}";

            return webClient.post()
                    .uri("/v1/sessions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, errorResponse -> {
                        log.error("JWT token retrieval failed with status code: {}", errorResponse.statusCode());
                        return Mono.error(new RuntimeException("Failed to retrieve a valid JWT"));
                    })
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>(){})
                    .map(responseMap -> {
                        String jwtToken = (String) responseMap.getOrDefault("token", "");

                        log.info("Checking JWT format: {}", isValidJwtFormat(jwtToken));

                        return AuthResponseDTO.builder()
                                .token(jwtToken)
                                .date(LocalDateTime.now())
                                .build();
                    });
        } catch (Exception ex) {
            log.error("An exception occurred while forming the request:", ex);
            return Mono.error(ex);
        }
    }

    /**
     * Проверка валидности формата JWT-токена.
     *
     * @param token Токен для проверки
     * @return True, если токен соответствует формату Base64, иначе false
     */
    public boolean isValidJwtFormat(String token) {
        if (token != null && token.contains(".") && token.split("\\.").length == 3) {
            return true;
        } else {
            log.warn("Invalid JWT format detected. Expected three parts in token.");
            throw new IllegalArgumentException("Expected JWT token to contain exactly three parts");
        }
    }

    /**
     * Автоматическое обновление токена каждые refresh.token.interval.ms с возможностью перезапуска при ошибках.
     */
    @Scheduled(fixedDelayString = "${refresh.token.interval.ms}")
    public void refreshToken() {
        AtomicInteger retryCounter = new AtomicInteger();

        log.info("Initiating token refresh process...");

        getJwtToken(env.getProperty(SECRET_KEY_PROPERTY_NAME))
                .doOnNext(authResponseDTO -> jwtTokenStore.setToken(authResponseDTO.getToken()))
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(5))
                        .doBeforeRetry(retrySignal -> {
                            int attemptNumber = retryCounter.incrementAndGet();
                            log.warn("Token refresh failed on attempt #{}", attemptNumber);
                        }))
                .onErrorResume(e -> {
                    log.error("All attempts to update the token have failed.", e);
                    return Mono.empty();
                })
                .block();

        log.info("Token refresh procedure completed.");
    }
}