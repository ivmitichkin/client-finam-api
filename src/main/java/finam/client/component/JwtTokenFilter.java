package finam.client.component;

import io.micrometer.common.lang.NonNullApi;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * Фильтр для добавления JWT-токена в каждый исходящий HTTP-запрос.
 * Токен извлекается из хранилища {@link JwtTokenStore}, и добавляется в заголовок AUTHORIZATION.
 * Если токен отсутствует, запрос отправляется без аутентификации.
 */
@Component
@AllArgsConstructor
@NonNullApi
@Slf4j
public class JwtTokenFilter implements ExchangeFilterFunction {

    private final JwtTokenStore jwtTokenStore;

    /**
     * Метод-фильтр, выполняющийся перед каждым запросом, проверяющий наличие и добавляющий JWT-токен.
     *
     * @param request исходный запрос, поступивший на фильтрацию
     * @param next следующая стадия обработки запроса
     * @return обработанный запрос с возможными изменениями (добавление токена)
     */
    @Override
    public Mono<ClientResponse> filter(
            ClientRequest request,
            ExchangeFunction next
    ) {

        URI uri = request.url();

        log.info("JWT Token filtering started for request: {}", uri);

        return Mono.justOrEmpty(jwtTokenStore.getToken())
                .doOnNext(token -> log.debug("Valid JWT token received"))

                .flatMap(token -> {
                    log.debug("Adding JWT token to headers of the request");
                    return next.exchange(ClientRequest.from(request)
                            .headers(h -> h.set(HttpHeaders.AUTHORIZATION, token))
                            .build());
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("No available JWT token, sending request without authentication");
                    return next.exchange(request);
                }));
    }

}