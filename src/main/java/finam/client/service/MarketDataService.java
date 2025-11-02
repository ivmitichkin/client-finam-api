package finam.client.service;

import finam.client.component.DateConverter;
import finam.client.dto.BarsResponseDTO;
import finam.client.dto.LatestTradesResponseDTO;
import finam.client.dto.OrderBookResponseDTO;
import finam.client.dto.QuoteResponseDTO;
import finam.client.enums.TimeFrameEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Сервис предназначен для выполнения запросов к внешнему API
 * и получения рыночных данных (котировок, торговых операций, стаканов заявок и т.п.).
 */
@Service
@Slf4j
public class MarketDataService {

    private final DateConverter dateConverter;
    private final WebClient authenticatedWebClient;

    public MarketDataService(DateConverter dateConverter, @Qualifier("authenticatedWebClient") WebClient authenticatedWebClient) {
        this.dateConverter = dateConverter;
        this.authenticatedWebClient = authenticatedWebClient;
    }

    /**
     * Возвращает последнюю доступную котировку для указанного финансового инструмента.
     *
     * @param symbol тикер финансового инструмента
     * @return Монореспонс последнего значения котировки
     */
    public Mono<QuoteResponseDTO> getLatestQuote(String symbol) {
        log.info("Fetching latest quote for symbol: {}", symbol);
        return authenticatedWebClient.get()
                .uri(uriBuilder -> uriBuilder.path("/v1/instruments/{symbol}/quotes/latest").build(symbol))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(QuoteResponseDTO.class);
    }

    /**
     * Возвращает список последних торгов по указанному финансовому инструменту.
     *
     * @param symbol тикер финансового инструмента
     * @return Монореспонс списка последних торгов
     */
    public Mono<LatestTradesResponseDTO> getLatestTrades(String symbol) {
        log.info("Fetching latest trades for symbol: {}", symbol);
        return authenticatedWebClient.get()
                .uri(uriBuilder -> uriBuilder.path("/v1/instruments/{symbol}/trades/latest").build(symbol))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(LatestTradesResponseDTO.class);
    }

    /**
     * Возвращает исторические бары (ценовые свечи) по заданному интервалу времени.
     *
     * @param symbol       тикер финансового инструмента
     * @param timeframe    временной период (например, минута, час, день)
     * @param intervalStart начальная дата интервала
     * @param intervalEnd   конечная дата интервала
     * @return Монореспонс исторических данных (баров)
     */
    public Mono<BarsResponseDTO> getHistoricalBars(String symbol, TimeFrameEnum timeframe, String intervalStart, String intervalEnd) {
        log.info("Fetching historical bars for symbol: {}, timeframe: {}, start: {}, end: {}",
                symbol, timeframe.name(), intervalStart, intervalEnd);
        return authenticatedWebClient.get()
                .uri(uriBuilder -> uriBuilder.path("/v1/instruments/{symbol}/bars")
                        .queryParam("timeframe", timeframe.value)
                        .queryParam("interval.start_time", dateConverter.convertToServerFormat(intervalStart))
                        .queryParam("interval.end_time", dateConverter.convertToServerFormat(intervalEnd))
                        .build(symbol))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(BarsResponseDTO.class);
    }

    /**
     * Возвращает стакан заявок (текущие цены покупки-продажи) для выбранного финансового инструмента.
     *
     * @param symbol тикер финансового инструмента
     * @return Монореспонс текущего стакана заявок
     */
    public Mono<OrderBookResponseDTO> getOrderBook(String symbol) {
        log.info("Fetching order book for symbol: {}", symbol);
        return authenticatedWebClient.get()
                .uri(uriBuilder -> uriBuilder.path("/v1/instruments/{symbol}/orderbook").build(symbol))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(OrderBookResponseDTO.class);
    }
}