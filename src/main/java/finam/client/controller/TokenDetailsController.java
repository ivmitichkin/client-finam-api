package finam.client.controller;


import finam.client.dto.TokenDetailsResponseDTO;
import finam.client.service.TokenDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Контроллер REST, предназначенный для обработки запросов, связанных с информацией о токене.
 */
@RestController
@AllArgsConstructor
@Tag(name = "Token Management", description = "API для операций с токеном аутентификации.")
@RequestMapping("/token")
public class TokenDetailsController {

    /**
     * Сервис, обеспечивающий бизнес-логику операций с деталями токена.
     */
    private final TokenDetailsService tokenDetailsService;

    /**
     * Получение данных сессии.
     *
     * @return Объект ResponseEntity с деталями токена и соответствующим статусом
     */
    @Operation(
            summary = "Получение информации о токене",
            description = """
                Используется для получения детальной информации о текущем токене аутентификации.
                Включает информацию о сроке действия токена и дополнительные метаданные.
                """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Детали токена успешно получены."),
                    @ApiResponse(responseCode = "404", description = "Токен не найден.")
            }
    )
    @PostMapping("/token_details")
    public Mono<ResponseEntity<TokenDetailsResponseDTO>> getTokenDetails() {
        return tokenDetailsService.getTokenDetails()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}