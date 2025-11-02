package finam.client.controller;

import finam.client.dto.AuthRequestDTO;
import finam.client.dto.AuthResponseDTO;
import finam.client.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер аутентификации по secret-токену.
 */
@AllArgsConstructor
@RestController
@Tag(name = "Authentication Controller",
        description = "Контроллер предназначен для управления процессом аутентификации.")
@RequestMapping("/auth")
public class AuthController {

    /**
     * Сервис аутентификации, используется для получения JWT-токенов.
     */
    private AuthService authService;

    /**
     * Обработчик POST-запросов на /login для аутентификации пользователя.
     * Возвращает JSON-ответ с токеном аутентификации.
     *
     * @param authRequest тело запроса, содержащее секретные данные для входа
     * @return успешный ответ с токеном или ошибка, если авторизация невозможна
     */
    @Operation(
            summary = "Авторизация пользователя",
            description = """
                Для авторизации пользователя необходимо передать секретный ключ.
                В случае успешной аутентификации, сервер возвращает JWT-токен, используемый для дальнейших запросов.
                """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешная аутентификация"),
                    @ApiResponse(responseCode = "400", description = "Ошибка аутентификации")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO authRequest) {
        AuthResponseDTO authResponseDTO = authService.getJwtToken(authRequest.getSecret()).block();

        if (authResponseDTO != null && authResponseDTO.getToken() != null) {
            return ResponseEntity.ok().body(authResponseDTO);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}