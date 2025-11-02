package finam.client.controller;

import finam.client.dto.GetAccountRequestDTO;
import finam.client.dto.GetAccountResponseDTO;
import finam.client.dto.TradesResponseDTO;
import finam.client.dto.TransactionsResponseDTO;

import finam.client.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Контроллер для получения информации о аккаунтах пользователя.
 */
@RestController
@RequestMapping("/accounts")
@Tag(name = "Accounts", description = "Управление учетными записями пользователей.")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    /**
     * Получение информации о конкретном аккаунте пользователя
     *
     * @param request Тело запроса с полем accountId (* обязательный параметр для идентификации пользователя)
     * @return Информация об аккаунте пользователя в виде ResponseEntity (* возвращает либо успешный ответ с информацией, либо ошибку 404 Not Found)
     */
    @Operation(
            summary = "Получение информации о счете пользователя",
            description = """
                Предоставляет подробную информацию о счёте пользователя по заданному идентификатору.
                Для этого необходим уникальный идентификатор счета, передаваемый в теле запроса.
                Возвращает полную информацию о счёте или ошибку 404, если счёт не найден.
                """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Информация о счёте найдена и возвращена успешно."),
                    @ApiResponse(responseCode = "404", description = "Запрошенный счёт не существует.")
            }
    )
    @PostMapping("/get_account_info")
    public Mono<ResponseEntity<GetAccountResponseDTO>> getAccountInfo(@RequestBody GetAccountRequestDTO request) {
        return accountService.getAccountInfo(request.getAccountId())
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Получение списка транзакций по конкретному аккаунту за определенный временной диапазон
     *
     * @param accountId Уникальный идентификатор аккаунта (* обязательный параметр)
     * @param startTime Начальное время диапазона (* обязательное значение в формате даты-времени)
     * @param endTime Конечное время диапазона (* обязательное значение в формате даты-времени)
     * @return Список транзакций в виде ResponseEntity (* возвращает список транзакций или ошибку 404 NOT FOUND)
     */
    @Operation(
            summary = "Получение истории транзакций по счету",
            description = """
                Возвращает историю транзакций по определённому счёту за заданный период времени.
                Требует передачи идентификатора счета и временных границ (начало и конец периода) yyyy-MM-dd, пример даты start_time: 2025-10-30, end_time: 2025-10-31.
                Если запрашиваемый счёт отсутствует или отсутствуют транзакции за указанный период, возвращает ошибку 404.
                """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список транзакций успешно возвращён."),
                    @ApiResponse(responseCode = "404", description = "Отсутствуют данные по запросу.")
            }
    )
    @GetMapping("/{accountId}/transactions")
    public Mono<ResponseEntity<TransactionsResponseDTO>> getAccountTransactions(
            @PathVariable String accountId,
            @RequestParam(name = "start_time") String startTime,
            @RequestParam(name = "end_time") String endTime) {

        return accountService.getAccountTransactions(accountId, startTime, endTime)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Получение списка торговых операций по конкретному аккаунту за указанный промежуток времени
     *
     * @param accountId Уникальный идентификатор аккаунта (* обязательный параметр)
     * @param startTime Начальное время диапазона (* обязательное значение в формате даты-времени)
     * @param endTime Конечное время диапазона (* обязательное значение в формате даты-времени)
     * @return Список торговых операций в виде ResponseEntity (* возвращает сделки или ошибку 404 NOT FOUND)
     */
    @Operation(
            summary = "Получение истории сделок по счету",
            description = """
                Возвращает историю торговых операций по определённому счёту за заданный период времени.
                Необходимо передать идентификатор счета и временные границы в формате yyyy-MM-dd, пример даты start_time: 2025-10-30, end_time: 2025-10-31.
                Отвечает списком всех сделок за указанный период или сообщением об ошибке 404, если нет данных.
                """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "История сделок успешно возвращена."),
                    @ApiResponse(responseCode = "404", description = "Нет данных по данному запросу.")
            }
    )
    @GetMapping("/{accountId}/trades")
    public Mono<ResponseEntity<TradesResponseDTO>> getAccountTrades(
            @PathVariable String accountId,
            @RequestParam(name = "start_time") String startTime,
            @RequestParam(name = "end_time") String endTime) {

        return accountService.getAccountTrades(accountId, startTime, endTime)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}