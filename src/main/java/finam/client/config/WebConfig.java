package finam.client.config;

import finam.client.component.JwtTokenFilter;
import finam.client.component.JwtTokenInterceptor;
import finam.client.component.JwtTokenStore;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Класс конфигурации веб-приложения для взаимодействия с удалёнными сервисами.
 */
@Configuration
public class WebConfig {

    @Value("${webclient.base-url}")
    private String baseUrl;

    /**
     * Создание базового WebClient-а, используемого для простых HTTP-запросов.
     * Используется аннотация @Primary, чтобы этот клиент считался основным среди прочих клиентов.
     *
     * @return экземпляр WebClient
     */
    @Bean
    @Primary
    public WebClient baseWebClient() {
        return WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .baseUrl(baseUrl)
                .build();
    }

    /**
     * Создание аутентифицированного WebClient-а, предназначенного для защищённых запросов,
     * использующего JWT-токены для авторизации.
     *
     * @param jwtTokenFilter фильтр обработки JWT-токенов
     * @return экземпляр WebClient с поддержкой авторизации
     */
    @Bean
    @Qualifier("authenticatedWebClient")
    public WebClient authenticatedWebClient(JwtTokenFilter jwtTokenFilter) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .filter(jwtTokenFilter)
                .build();
    }

    /**
     * Создание настроенного экземпляра OkHttpClient с поддержкой JWT-аутентификации.
     * Клиент добавляет JWT-токены в каждый запрос благодаря специально разработанному фильтру-интерцептору.
     * Использует ограничения на выполнения запросов - connectTimeout, readTimeout и writeTimeout.
     *
     * @param jwtTokenStore хранилище JWT-токенов
     * @return настроенный экземпляр OkHttpClient с возможностью авто-вставки токенов
     */
    @Bean
    public OkHttpClient okHttpClient(JwtTokenStore jwtTokenStore) {
        JwtTokenInterceptor jwtInterceptor = new JwtTokenInterceptor(jwtTokenStore);
        return new OkHttpClient.Builder()
                .addInterceptor(jwtInterceptor)
                .connectTimeout(30, SECONDS)
                .readTimeout(30, SECONDS)
                .writeTimeout(60, SECONDS)
                .build();
    }
}