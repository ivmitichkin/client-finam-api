package finam.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * Основной класс приложения, точка входа для запуска Spring Boot-приложения.
 *
 */
@SpringBootApplication
@EnableScheduling
@ConditionalOnProperty(name = "secret.key")
public class FinamClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinamClientApplication.class, args);
    }

}
