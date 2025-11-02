package finam.client.controller;

import finam.client.dto.BarsResponseDTO;
import finam.client.dto.LatestTradesResponseDTO;
import finam.client.dto.OrderBookResponseDTO;
import finam.client.dto.QuoteResponseDTO;
import finam.client.enums.TimeFrameEnum;
import finam.client.service.MarketDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


/**
 * Контроллер для отображения данных по последним котировкам, торговым операциям, историческим данным и стаканам заявок.
 */
@RestController
@RequestMapping("/market_data")
@AllArgsConstructor
@Tag(name = "Market Data", description = "Работа с рыночными данными.")
public class MarketDataController {

    private final MarketDataService marketDataService;

    /**
     * Запрашивает и возвращает последнюю котировку для указанного символа (финансового инструмента).
     *
     * @param symbol тикер финансового инструмента
     * @return успешный ответ с котировкой либо ошибка 'Not Found', если инструмент не найден
     */
    @Operation(
            summary = "Получение последней котировки инструмента",
            description = """
                Предоставляет самую свежую цену (котировку) выбранного финансового инструмента.
                Для этого необходимо передать тикер инструмента в параметрах пути.
                Возвращает котировку или ошибку 404, если указанный инструмент не найден.
                """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Последняя котировка доступна."),
                    @ApiResponse(responseCode = "404", description = "Финансовый инструмент не найден.")
            }
    )
    @GetMapping("/last-quote/{symbol}")
    public Mono<ResponseEntity<QuoteResponseDTO>> getLatestQuote(@PathVariable String symbol) {
        return marketDataService.getLatestQuote(symbol)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Возвращает список последних трейдов по указанному финансовому инструменту.
     *
     * @param symbol тикер финансового инструмента
     * @return успешный ответ с последними сделками либо ошибка 'Not Found'
     */
    @Operation(
            summary = "Получение последних сделок по инструменту",
            description = """
                Возвращает последние торговые сделки по выбранному финансовому инструменту.
                Передайте тикер инструмента в параметре пути.
                Сообщает последние сделки или ошибку 404, если инструмент не найден.
                """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Последние сделки найдены."),
                    @ApiResponse(responseCode = "404", description = "Указанный финансовый инструмент не обнаружен.")
            }
    )
    @GetMapping("/{symbol}/trades/latest")
    public Mono<ResponseEntity<LatestTradesResponseDTO>> getLatestTrades(@PathVariable String symbol) {
        return marketDataService.getLatestTrades(symbol)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Возврат исторических ценовых свечей (баров) для определенного периода времени.
     *
     * @param symbol       тикер финансового инструмента
     * @param timeframe    временная рамка (минуты, часы, дни)
     * @param intervalStart начало временного диапазона
     * @param intervalEnd   конец временного диапазона
     * @return успешный ответ с историческими данными или ошибка 'Not Found' при отсутствии данных
     */
    @Operation(
            summary = "Получение исторических данных цены",
            description = """
                Загружает исторические данные (ценовые свечи) для указанного финансового инструмента за требуемый интервал времени.
                Параметры включают название инструмента, временное окно и интервалы дат начала и окончания.
                Вернёт исторические данные или ошибку 404, если данные не обнаружены.
                """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Исторические данные успешно загружены."),
                    @ApiResponse(responseCode = "404", description = "Данные не найдены.")
            }
    )
    @GetMapping("/bars/{symbol}")
    public Mono<ResponseEntity<BarsResponseDTO>> getHistoricalData(
            @PathVariable String symbol,
            @RequestParam TimeFrameEnum timeframe,
            @RequestParam String intervalStart,
            @RequestParam String intervalEnd
    ) {
        return marketDataService.getHistoricalBars(symbol, timeframe, intervalStart, intervalEnd)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Возвращает стакан заявок (текущие лимитные ордера на покупку и продажу) для указанного инструмента.
     *
     * @param symbol тикер финансового инструмента
     * @return успешный ответ с информацией о заявках или ошибка 'Not Found'
     */
    @Operation(
            summary = "Получение стакана заявок (биржевого ордера)",
            description = """
                Отправляет заявку на получение текущего стакана рыночных заказов для указанного инструмента.
                Необходимо передать тикер инструмента в параметрах пути.
                Ответ включает книгу приказов или ошибку 404, если инструмент не найден.
                """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Стакан заявок доступен."),
                    @ApiResponse(responseCode = "404", description = "Финансовый инструмент не найден.")
            }
    )
    @GetMapping("/order_book/{symbol}")
    public Mono<ResponseEntity<OrderBookResponseDTO>> getOrderBook(@PathVariable String symbol) {
        return marketDataService.getOrderBook(symbol)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}