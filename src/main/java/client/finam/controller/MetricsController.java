package client.finam.controller;

import client.finam.dto.GetUsageMetricsResponse;
import client.finam.service.MetricsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Контроллер для управления операциями с метриками использования API.
 * Предоставляет конечные точки REST API для получения текущих показателей использования сервисов API.
 */
@AllArgsConstructor
@RestController
@RequestMapping("/metrics")
@Tag(name = "Metrics", description = "Операции с Метриками.")
public class MetricsController {

    private final MetricsService metricsService;

    /**
     * Метод для получения текущих метрик использования API.
     *
     * @return Моновыражение с объектом {@link ResponseEntity}, содержащим ответ с метриками использования API.
     * Если метрики отсутствуют, возвращает ответ с кодом 404 (Not Found).
     */
    @Operation(
            summary = "Получает список с количеством доступных запросов к каждому из endpoint's API",
            description = """
                В ответе массив формата:
                    {
                          "quotas": [
                            {
                              "name": "UsageMetricsService.getUsageMetrics",
                              "limit": 200,
                              "remaining": 199,
                              "reset_time": "2026-01-02T12:59:16.961Z"
                            },
                            {
                              "name": "MarketDataService.subscribeBars",
                              "limit": 200,
                              "remaining": 200,
                              "reset_time": null
                            },...
                """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Метрики получены."),
                    @ApiResponse(responseCode = "404", description = "Ничего не найдено.")
            }
    )
    @GetMapping(value = "/usage")
    public Mono<ResponseEntity<GetUsageMetricsResponse>> getCurrentUsageMetrics() {
        return metricsService.getUsageMetrics()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}