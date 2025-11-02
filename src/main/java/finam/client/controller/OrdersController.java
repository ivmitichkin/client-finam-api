package finam.client.controller;

import finam.client.dto.OrderDTO;
import finam.client.dto.OrderStateDTO;
import finam.client.dto.OrdersResponseDTO;
import finam.client.service.OrdersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Контроллер для управления операциями с ордерами.
 * Обеспечивает взаимодействие с сервисом через конечные точки REST API.
 */
@RestController
@RequestMapping("/orders")
@AllArgsConstructor
@Tag(name = "Orders", description = "Операции с ордерами.")
public class OrdersController {

    private final OrdersService ordersService;

    /**
     * Метод для получения списка ордеров по уникальному идентификатору аккаунта.
     *
     * @param accountId идентификатор аккаунта
     * @return список ордеров в виде моновыражения с объектом ResponseEntity
     */
    @Operation(
            summary = "Получение списка ордеров по учётной записи",
            description = """
                Получает полный список активных ордеров для указанного идентификатора учёта.
                Идентификатор передается в качестве параметра пути.
                Возвращает список текущих ордеров или ошибку 404, если ничего не найдено.
                """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ордеры найдены и отправлены."),
                    @ApiResponse(responseCode = "404", description = "Ничего не найдено.")
            }
    )
    @GetMapping("/{account_id}")
    public Mono<ResponseEntity<OrdersResponseDTO>> getOrders(@PathVariable("account_id") String accountId) {
        return ordersService.getOrders(accountId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Метод для получения состояния конкретного ордера по уникальным идентификаторам аккаунта и ордера.
     *
     * @param accountId идентификатор аккаунта
     * @param orderId   идентификатор ордера
     * @return состояние ордера в виде моновыражения с объектом ResponseEntity
     */
    @Operation(
            summary = "Проверка статуса ордера",
            description = """
                Проверяет статус конкретного ордера по указанным идентификаторам учетной записи и ордера.
                Параметры отправляются через URI.
                Возвращает статус ордера или ошибку 404, если ордер не найден.
                """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ордер найден и его статус отправлен."),
                    @ApiResponse(responseCode = "404", description = "Запрашиваемый ордер не найден.")
            }
    )
    @GetMapping("/{account_id}/{order_id}")
    public Mono<ResponseEntity<OrderStateDTO>> getOrder(@PathVariable("account_id") String accountId,
                                                        @PathVariable("order_id") String orderId) {
        return ordersService.getOrder(accountId, orderId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Метод для отмены существующего ордера по уникальным идентификаторам аккаунта и ордера.
     *
     * @param accountId идентификатор аккаунта
     * @param orderId   идентификатор ордера
     * @return статус успешного удаления ордера в виде моновыражения с объектом ResponseEntity
     */
    @Operation(
            summary = "Отмена ордера",
            description = """
                Производит отмену активного ордера по идентификаторам аккаунта и ордера.
                Параметры отправляются через URI.
                Возвращает обновленный статус ордера или пустое тело ответа (204 No Content), если ордер успешно удалён.
                """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ордер успешно отменён."),
                    @ApiResponse(responseCode = "204", description = "Задача выполнена, однако данные отсутствовали."),
                    @ApiResponse(responseCode = "404", description = "Ордер не найден.")
            }
    )
    @DeleteMapping("/{account_id}/{order_id}")
    public Mono<ResponseEntity<OrderStateDTO>> cancelOrder(@PathVariable("account_id") String accountId,
                                                           @PathVariable("order_id") String orderId) {
        return ordersService.cancelOrder(accountId, orderId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }

    /**
     * Метод для размещения нового ордера на указанный аккаунт.
     *
     * @param order     объект ордера, содержащий необходимые данные
     * @param accountId идентификатор аккаунта
     * @return результат размещения ордера в виде моновыражения с объектом ResponseEntity
     */
    @Operation(
            summary = "Размещение нового ордера",
            description = """
                Создаёт новый ордер для указанной учетной записи.
                Ордер отправляется в теле запроса, а идентификатор учетной записи указывается в параметрах маршрута.
                Возвращает созданный ордер или ошибку 400, если возникли проблемы с созданием.
                """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Новый ордер размещён успешно."),
                    @ApiResponse(responseCode = "400", description = "Проблемы с созданием ордера.")
            },
            tags = {"Orders"}
    )
    @PostMapping("/{account_id}/place_order")
    public Mono<ResponseEntity<OrderStateDTO>> placeOrder(@RequestBody OrderDTO order,
                                                          @PathVariable("account_id") String accountId) {
        return ordersService.placeOrder(order, accountId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }
}