package finam.client.service;

import finam.client.component.DateConverter;
import finam.client.dto.GetAccountResponseDTO;
import finam.client.dto.TradesResponseDTO;
import finam.client.dto.TransactionsResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 *Сервис для работы с аккаунтами пользователя.
 */
@Service
public class AccountService {

    private final DateConverter dateConverter;
    private final WebClient authenticatedWebClient;

    public AccountService(DateConverter dateConverter, @Qualifier("authenticatedWebClient") WebClient authenticatedWebClient) {
        this.dateConverter = dateConverter;
        this.authenticatedWebClient = authenticatedWebClient;
    }

    /**
     * Метод для получения информации об аккаунте пользователя.
     *
     * @param accountId ID аккаунта - обязательный параметр для идентификации пользователя.
     * @return Ответ сервера в виде GetAccountResponseDTO  монодату возвращаемого значения.
     */
    public Mono<GetAccountResponseDTO> getAccountInfo(String accountId) {
        return authenticatedWebClient.get()
                .uri(uriBuilder -> uriBuilder.path("/v1/accounts/{accountId}")
                        .build(accountId))
                .retrieve()
                .bodyToMono(GetAccountResponseDTO.class);
    }

    /**
     * Получение транзакций по счету за указанный период.
     *
     * @param accountId   ID счета - идентификационный номер аккаунта.
     * @param startTime   начало периода - должно быть передано в правильном формате.
     * @param endTime     конец периода - также передается в нужном формате.
     * @return Объект TransactionsResponseDTO - монообертка вокруг результата.
     */
    public Mono<TransactionsResponseDTO> getAccountTransactions(String accountId, String startTime, String endTime) {
        return authenticatedWebClient.get()
                .uri(uriBuilder -> uriBuilder.path("/v1/accounts/{accountId}/transactions")
                        .queryParam("interval.start_time", dateConverter.convertToServerFormat(startTime))
                        .queryParam("interval.end_time", dateConverter.convertToServerFormat(endTime))
                        .build(accountId))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(TransactionsResponseDTO.class);
    }

    /**
     * Получение торговых операций по указанному счету за заданный интервал
     *
     * @param accountId   ID счета - уникальный идентификатор аккаунта.
     * @param startTime   начало интервала - обязательно передать в верном формате.
     * @param endTime     конец интервала - тоже обязателен верный формат.
     * @return Результат в виде TradesResponseDTO - монообертка вокруг результирующего объекта.
     */
    public Mono<TradesResponseDTO> getAccountTrades(String accountId, String startTime, String endTime) {
        return authenticatedWebClient.get()
                .uri(uriBuilder -> uriBuilder.path("/v1/accounts/{accountId}/trades")
                        .queryParam("interval.start_time", dateConverter.convertToServerFormat(startTime))
                        .queryParam("interval.end_time", dateConverter.convertToServerFormat(endTime))
                        .build(accountId))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(TradesResponseDTO.class);
    }
}
