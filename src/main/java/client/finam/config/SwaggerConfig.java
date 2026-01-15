package client.finam.config;

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
@OpenAPIDefinition(info = @Info(title = "Java-клиент для Finam REST API",
        version = "1.1.1",
        description = """
                Клиентская библиотека для взаимодействия с сервисами Finam посредством REST API.<br/>
                Документация доступна по ссылке: <a href=\"https://tradeapi.finam.ru/docs/guides/rest\">ссылка на спецификацию</a><br/>
                Актуальная версия Finam - Версия 2.10.0 (11.12.2025)
              """),
        security = {@SecurityRequirement(name = "api_key")})
@SecurityScheme(name = "api_key",
        description = "API Key Authentication",
        in = SecuritySchemeIn.HEADER,
        type = SecuritySchemeType.APIKEY,
        paramName = "X-API-KEY")
public class SwaggerConfig {
}
