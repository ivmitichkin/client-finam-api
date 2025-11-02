package finam.client.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.nio.charset.StandardCharsets;

/**
 * Глобальный обработчик исключений для централизованной обработки ошибок,
 * возникающих в контроллерах приложения. Класс помечен аннотацией
 * {@link ControllerAdvice}, что позволяет перехватывать исключения,
 * выбрасываемые контроллерами, и формировать унифицированный ответ клиенту.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обработка исключений при ошибочных запросах через WebClient.
     */
    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<String> handleWebClientResponseErrors(WebClientResponseException ex) {
        String rawResponse = new String(ex.getResponseBodyAsByteArray(), StandardCharsets.UTF_8);
        return new ResponseEntity<>(rawResponse, ex.getStatusCode());
    }

    /**
     * Обработка общих исключений при ошибочных запросах (например, плохих URI, нехватки параметров и т.п.).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralErrors(Exception ex) {
        return new ResponseEntity<>("Возникла внутренняя ошибка сервера: " + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}