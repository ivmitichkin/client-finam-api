package finam.client.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;


/**
 * Конфигурационный класс для интеграции OpenAPI/Swagger в приложении.
 * Определяет базовую конфигурацию документации API:
 * - Название приложения ("Java Finam client application").
 * - Версия API ("v1").
 * - Требование безопасности: используется заголовок "X-API-KEY" для авторизации по API key.
 *
 * @version v1
 */
@Configuration
@OpenAPIDefinition(info = @Info(title = "Java Finam client application", version = "v1"),
        security = {@SecurityRequirement(name = "api_key")})
@SecurityScheme(name = "api_key",
        description = "API Key Authentication",
        in = SecuritySchemeIn.HEADER,
        type = SecuritySchemeType.APIKEY,
        paramName = "X-API-KEY")
public class SwaggerConfig {
}
