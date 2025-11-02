package finam.client.component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;
import java.util.List;

/**
 * Фильтр для проверки наличия действительного API-ключа в каждом поступающем HTTP-запросе.
 * Реализует механизм защиты путем предварительной проверки входящих запросов на наличие валидного API-ключа,
 * хранящегося в настройках приложения. Если ключ отсутствует или недействителен,
 * запросы отклоняются с соответствующим статусом ошибки.
 */
@Component
@AllArgsConstructor
@Slf4j
public class ApiKeyFilter implements Filter {

    private final String EXCLUDED_PATHS = "excluded.paths";
    private ApiKeyValidator validator;

    @Value("${" + EXCLUDED_PATHS + ":}")
    private List<String> excludedPaths;


    /**
     * Метод инициализации фильтра.
     *
     * @param filterConfig конфигурация фильтра
     */
    @Override
    public void init(FilterConfig filterConfig) {
        log.debug("Initializing ApiKeyFilter");
    }

    /**
     * Метод завершения работы фильтра.
     */
    @Override
    public void destroy() {
        log.debug("Destroying ApiKeyFilter");
    }

    /**
     * Основной метод обработки запросов.
     *
     * @param servletRequest  запрос клиента
     * @param servletResponse ответ клиенту
     * @param chain           цепочка фильтров
     */

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) {
        HttpServletRequest httpRequest = null;
        HttpServletResponse httpResponse = null;

        try {
            httpRequest = (HttpServletRequest) servletRequest;
            httpResponse = (HttpServletResponse) servletResponse;

            for (String excludedPath : excludedPaths) {
                if (new AntPathMatcher().match(excludedPath, httpRequest.getRequestURI())) {
                    log.debug("Path {} matches exclusion pattern {}", httpRequest.getRequestURI(), excludedPath);
                    chain.doFilter(httpRequest, httpResponse);
                    return;
                }
            }

            String errorMessage = validator.validateApiKey(httpRequest);

            if (!errorMessage.isEmpty()) {
                log.warn("Invalid API key detected from IP: {}, Error message: {}", httpRequest.getRemoteAddr(), errorMessage);
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
                writeJsonResponse(httpResponse, "{\"message\": \"" + errorMessage + "\"}");
                return;
            }

            chain.doFilter(httpRequest, httpResponse);

        } catch (IOException | ServletException e) {
            handleException(e, httpResponse);
        }
    }

    /**
     * Вспомогательный метод для безопасной записи JSON-ответа в выходной поток.
     *
     * @param response объект ответа
     * @param jsonData строка с JSON-данными
     */
    private void writeJsonResponse(HttpServletResponse response, String jsonData) {
        try {
            response.getWriter().write(jsonData);
        } catch (IOException ex) {
            log.error("Failed to write JSON response", ex);
        }
    }

    /**
     * Обработчик исключений, возникающих при обработке запроса.
     *
     * @param exception возникшее исключение
     * @param response  объект ответа для формирования соответствующего статуса
     */
    private void handleException(Exception exception, HttpServletResponse response) {
        log.error("An error occurred while processing the request", exception);
        try {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
        } catch (IOException ioException) {
            log.error("Failed to send error response", ioException);
        }
    }
}
