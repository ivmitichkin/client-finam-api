package finam.client.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Класс для хранения и управления JWT-токеном.
 */
@Component
@Slf4j
public class JwtTokenStore {

    private final AtomicReference<String> tokenRef = new AtomicReference<>();

    /**
     * Устанавливает новый JWT-токен в хранилище.
     * Выполняется синхронизировано для предотвращения одновременных обновлений.
     *
     * @param token Новый токен для установки
     */
    public synchronized void setToken(String token) {
        log.info("New token has been set.");
        tokenRef.set(token);
    }

    /**
     * Возвращает текущий JWT-токен, если он существует.
     * Для защиты конфиденциальности токена выводится лишь его начало.
     *
     * @return Опциональный объект с токеном, если он установлен
     */
    public Optional<String> getToken() {
        String currentToken = tokenRef.get();
        if (currentToken != null) {
            log.debug("Existing token retrieved: {}", currentToken.substring(0, 8) + "...");
        } else {
            log.warn("No token present!");
        }
        return Optional.ofNullable(currentToken);
    }
}