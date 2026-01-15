package client.finam;

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
@ConditionalOnProperty(name = "secret.finam.key")
public class ClientFinamApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientFinamApplication.class, args);
    }

}
