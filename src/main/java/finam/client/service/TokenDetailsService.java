package finam.client.service;

import finam.client.component.JwtTokenStore;
import finam.client.dto.TokenDetailsResponseDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


/**
 * Сервис для обработки операций с деталями токенов аутентификации.
 */
@Service
@Slf4j
@AllArgsConstructor
public class TokenDetailsService {

    private final WebClient webClient;
    private final JwtTokenStore jwtTokenStore;

    /**
     * Метод возвращает детали токена аутентификации путем отправки POST-запроса на сервер.
     * Выполняет асинхронный HTTP-запрос к API для получения детальной информации о токене,
     * используя сохранённый токен из хранилища {@link JwtTokenStore}. Возвращает объект типа
     * {@link TokenDetailsResponseDTO}, содержащий подробности токена.
     *
     * @return Поток ({@code Mono}) с объектом {@link TokenDetailsResponseDTO},
     *         содержащим информацию о токене.
     */
    public Mono<TokenDetailsResponseDTO> getTokenDetails() {
        String body = "{\"token\": \"" + jwtTokenStore.getToken().orElse("") + "\"}";

        log.info("Received request for retrieving token details");
        try {
            return webClient.post()
                    .uri("/v1/sessions/details")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(TokenDetailsResponseDTO.class)
                    .doOnSuccess(tokenDetails -> log.info("Successfully retrieved token details"))
                    .doOnError(error -> log.error("Error fetching token details: ", error));
        } catch (Exception e) {
            log.error("Exception during processing request: ", e);
            throw new RuntimeException(e);
        }
    }
}