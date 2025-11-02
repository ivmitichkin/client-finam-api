package finam.client.component;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Сервис для проверки действительности API-ключа, передаваемого в HTTP-заголовке.
 * Используется совместно с фильтром {@link ApiKeyFilter}. Осуществляется сравнение предоставленного API-ключа
 * с заранее заданным хешированным значением из настроек окружения.
 */
@Component
@AllArgsConstructor
public class ApiKeyValidator {

    private final Environment env;

    private static final String API_KEY_HEADER_NAME = "X-API-KEY";
    private static final String HASH_API_KEY = "hash.api.key";

    /**
     * Валидирует API-ключ и возвращает конкретное сообщение об ошибке.
     *
     * @param request текущий запрос
     * @return сообщение об ошибке или пустая строка, если ключ корректен.
     */
    public String validateApiKey(HttpServletRequest request) {
        String providedApiKey = request.getHeader(API_KEY_HEADER_NAME);

        if (providedApiKey == null || providedApiKey.trim().isEmpty()) {
            return "No API key was provided.";
        }

        if (!BCrypt.checkpw(providedApiKey, env.getProperty(HASH_API_KEY))) {
            return "Provided API key does not match our records.";
        }

        return "";
    }
}