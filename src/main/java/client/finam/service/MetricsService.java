package client.finam.service;

import client.finam.dto.GetUsageMetricsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Сервис для взаимодействия с API получения метрик использования api(Metrics Service).
 * Реализует методы для отправки запросов к внешнему API Finam и получения актуальной информации о лимитах и ограничениях API.
 */
@Service
@Slf4j
public class MetricsService {

    private final WebClient webClient;

    public MetricsService(@Qualifier("authenticatedWebClient") WebClient authenticatedWebClient) {
        this.webClient = authenticatedWebClient;
    }

    /**
     * Метод отправляет GET-запрос к серверу API Finam для получения текущих метрик использования API.
     *
     * @return Mono<GetUsageMetricsResponse> объект с информацией о лимитах использования api.
     */
    public Mono<GetUsageMetricsResponse> getUsageMetrics() {
        return webClient.get()
                .uri("/v1/usage")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> {
                    log.error("Error fetching API usage metrics: {}", clientResponse.statusCode());
                    return clientResponse.bodyToMono(String.class).flatMap(errorBody -> {
                        log.error("Error body: {}", errorBody);
                        return Mono.error(new RuntimeException("Error retrieving metrics."));
                    });
                })
                .bodyToMono(GetUsageMetricsResponse.class)
                .doOnSuccess(response -> log.info("Received API usage metrics successfully."))
                .doOnError(e -> log.error("An error occurred while processing the request:", e));
    }
}