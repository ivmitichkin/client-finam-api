package finam.client.service;

import finam.client.dto.OrderDTO;
import finam.client.dto.OrderStateDTO;
import finam.client.dto.OrdersResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Сервис для взаимодействия с внешним REST-сервисом ордеров.
 * Предоставляет методы для получения списка ордеров, отдельного ордера, отмены ордера и размещения нового ордера.
 */
@Service
@Slf4j
public class OrdersService {

    private final WebClient authenticatedWebClient;

    public OrdersService(@Qualifier("authenticatedWebClient") WebClient authenticatedWebClient) {
        this.authenticatedWebClient = authenticatedWebClient;
    }

    /**
     * Метод для получения списка ордеров по указанному идентификатору аккаунта.
     *
     * @param accountId уникальный идентификатор аккаунта
     * @return объект-обертка с информацией обо всех ордерах указанного аккаунта
     */
    public Mono<OrdersResponseDTO> getOrders(String accountId) {
        log.info("Fetching orders for account ID: {}", accountId);
        return authenticatedWebClient.get()
                .uri(uriBuilder -> uriBuilder.path("/v1/accounts/{account_id}/orders").build(accountId))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(OrdersResponseDTO.class);
    }

    /**
     * Метод для получения состояния конкретного ордера по указанным идентификаторам аккаунта и ордера.
     *
     * @param accountId уникальный идентификатор аккаунта
     * @param orderId   уникальный идентификатор ордера
     * @return объект с состоянием выбранного ордера
     */
    public Mono<OrderStateDTO> getOrder(String accountId, String orderId) {
        log.info("Fetching state of order {} for account ID: {}", orderId, accountId);
        return authenticatedWebClient.get()
                .uri(uriBuilder -> uriBuilder.path("/v1/accounts/{account_id}/orders/{order_id}").build(accountId, orderId))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(OrderStateDTO.class);
    }

    /**
     * Метод для отмены существующего ордера по указанным идентификаторам аккаунта и ордера.
     *
     * @param accountId уникальный идентификатор аккаунта
     * @param orderId   уникальный идентификатор ордера
     * @return объект с обновленным статусом отмененного ордера
     */
    public Mono<OrderStateDTO> cancelOrder(String accountId, String orderId) {
        log.info("Cancelling order {} for account ID: {}", orderId, accountId);
        return authenticatedWebClient.delete()
                .uri(uriBuilder -> uriBuilder.path("/v1/accounts/{account_id}/orders/{order_id}").build(accountId, orderId))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(OrderStateDTO.class);
    }

    /**
     * Метод для размещения нового ордера на указанный аккаунт.
     *
     * @param order     объект ордера, содержащий необходимую информацию
     * @param accountId уникальный идентификатор аккаунта
     * @return объект с результатом операции размещения ордера
     */
    public Mono<OrderStateDTO> placeOrder(OrderDTO order, String accountId) {
        log.info("Placing new order for account ID: {}", accountId);
        return authenticatedWebClient.post()
                .uri(uriBuilder -> uriBuilder.path("/v1/accounts/{account_id}/orders").build(accountId))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(order)
                .retrieve()
                .bodyToMono(OrderStateDTO.class);
    }
}