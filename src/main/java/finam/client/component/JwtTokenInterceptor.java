package finam.client.component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Интерцептор для добавления JWT-токена в каждый исходящий HTTP-запрос.
 * Токен извлекается из хранилища {@link JwtTokenStore}, и добавляется в заголовок AUTHORIZATION.
 * Если токен отсутствует, запрос отправляется без аутентификации.
 */
@AllArgsConstructor
@Component
@Slf4j
public class JwtTokenInterceptor implements Interceptor {

    private final JwtTokenStore jwtTokenStore;

    /**
     * Основной метод-интерцептор, запускаемый перед каждым запросом для проверки и возможного добавления JWT-токена.
     *
     * @param chain объект, представляющий цепочку обработки запросов
     * @return ответ на запрос с возможным изменением заголовков (добавление токена)
     */
    @NotNull
    @Override
    public Response intercept(Interceptor.Chain chain) {
        Request originalRequest = chain.request();
        String url = originalRequest.url().toString();

        log.info("JWT token filtering started for request: {}", url);

        try {
            String token = jwtTokenStore.getToken().orElse("");

            if (!token.isEmpty()) {
                log.debug("Valid JWT token obtained");

                Request modifiedRequest = originalRequest.newBuilder()
                        .header("Authorization", token)
                        .build();

                log.debug("Adding JWT token to request headers");

                try {
                    return chain.proceed(modifiedRequest);
                } catch (IOException ioe) {
                    log.error("I/O error when proceeding with modified request", ioe);
                    return createErrorResponse(ioe, 500, "Internal Server Error");
                }
            } else {
                log.warn("No valid JWT token found, proceeding with unauthenticated request");

                try {
                    return chain.proceed(originalRequest);
                } catch (IOException ioe) {
                    log.error("I/O error when proceeding with original request", ioe);
                    return createErrorResponse(ioe, 500, "Internal Server Error");
                }
            }
        } catch (RuntimeException re) {
            log.error("Unexpected runtime error during token handling", re);
            return createErrorResponse(re, 500, "Internal Server Error");
        }
    }

    /**
     * Создаёт ответ с указанным кодом и сообщением об ошибке.
     *
     * @param cause  Причина ошибки (исключение)
     * @param code   Код ответа
     * @param message Сообщение об ошибке
     * @return Ответ с установленным кодом и телом
     */
    private Response createErrorResponse(Throwable cause, int code, String message) {
        return new Response.Builder()
                .request(new Request.Builder().url("").build())
                .protocol(okhttp3.Protocol.HTTP_1_1)
                .code(code)
                .message(cause.getLocalizedMessage())
                .body(okhttp3.ResponseBody.create(message, okhttp3.MediaType.get("text/plain")))
                .build();
    }
}