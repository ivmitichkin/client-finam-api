package finam.client.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import finam.client.dto.AssetsResponseDTO;
import finam.client.dto.ClockResponseDTO;
import finam.client.dto.ExchangesResponseDTO;
import finam.client.dto.GetAssetParamsResponseDTO;
import finam.client.dto.GetAssetResponseDTO;
import finam.client.dto.OptionsChainResponseDTO;
import finam.client.dto.ScheduleResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ToolsService {

    private final WebClient authenticatedWebClient;
    private final ObjectMapper objectMapper;

    public ToolsService(@Qualifier("authenticatedWebClient") WebClient authenticatedWebClient, ObjectMapper objectMapper) {
        this.authenticatedWebClient = authenticatedWebClient;
        this.objectMapper = objectMapper;
    }

    /**
     * Метод для получения полного списка финансовых инструментов (активов).
     *
     * @return объект-обертка с полным списком активов
     */
    public Mono<ResponseEntity<AssetsResponseDTO>> getAssets() {
        log.info("Fetching assets list");
        return authenticatedWebClient.get()
                .uri("https://api.finam.ru/v1/assets")
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(clientResponse -> {
                    if (!clientResponse.statusCode().is2xxSuccessful()) {
                        return clientResponse.bodyToMono(String.class)
                                .flatMap(text -> {
                                    log.error("Error fetching assets: Status={}, Body={}",
                                            clientResponse.statusCode(), text);
                                    return Mono.error(new RuntimeException("Error fetching assets: " +
                                            clientResponse.statusCode() + ": " + text));
                                });
                    }
                    return clientResponse.bodyToMono(AssetsResponseDTO.class)
                            .map(dto -> ResponseEntity.status(clientResponse.statusCode()).body(dto));
                });
    }

    /**
     * Метод для получения текущего серверного времени от API Finam.
     *
     * @return объект-обертка с информацией о серверном времени
     */
    public Mono<ResponseEntity<ClockResponseDTO>> getServerTime() {
        log.info("Fetching server time");
        return authenticatedWebClient.get()
                .uri("https://api.finam.ru/v1/assets/clock")
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(json -> {
                    try {
                        ClockResponseDTO clockResponseDTO = objectMapper.readValue(json, ClockResponseDTO.class);
                        return Mono.just(ResponseEntity.ok(clockResponseDTO));
                    } catch (Exception ex) {
                        log.error("Error parsing JSON to ClockResponseDTO", ex);
                        return Mono.error(ex);
                    }
                });
    }

    /**
     * Метод для получения информации о конкретном финансовом инструменте (акции).
     *
     * @param symbol   символ финансового инструмента
     * @param accountId уникальный идентификатор аккаунта
     * @return объект-обертка с информацией о выбранном активе
     */
    public Mono<ResponseEntity<GetAssetResponseDTO>> getAsset(String symbol, String accountId) {
        log.info("Fetching asset information for symbol: {}, account ID: {}", symbol, accountId);
        return authenticatedWebClient.get()
                .uri(uriBuilder -> uriBuilder.path("/v1/assets/" + symbol)
                        .queryParam("account_id", accountId).build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(json -> {
                    try {
                        GetAssetResponseDTO dto = objectMapper.readValue(json, GetAssetResponseDTO.class);
                        return Mono.just(ResponseEntity.ok(dto));
                    } catch (Exception ex) {
                        log.error("Error parsing JSON to GetAssetResponseDTO", ex);
                        return Mono.error(ex);
                    }
                });
    }

    /**
     * Метод для получения списка торговых площадок (бирж).
     *
     * @return объект-обертка с перечнем существующих бирж
     */
    public Mono<ResponseEntity<ExchangesResponseDTO>> getExchanges() {
        log.info("Fetching exchanges list");
        return authenticatedWebClient.get()
                .uri("https://api.finam.ru/v1/exchanges")
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(json -> {
                    try {
                        ExchangesResponseDTO dto = objectMapper.readValue(json, ExchangesResponseDTO.class);
                        return Mono.just(ResponseEntity.ok(dto));
                    } catch (Exception ex) {
                        log.error("Error parsing JSON to ExchangesResponseDTO", ex);
                        return Mono.error(ex);
                    }
                });
    }

    /**
     * Метод для получения параметров конкретного финансового инструмента.
     *
     * @param symbol   символ финансового инструмента
     * @param accountId уникальный идентификатор аккаунта
     * @return объект-обертка с параметрами выбранного актива
     */
    public Mono<ResponseEntity<GetAssetParamsResponseDTO>> getAssetParam(String symbol, String accountId) {
        log.info("Fetching parameters for asset: {}, account ID: {}", symbol, accountId);
        return authenticatedWebClient.get()
                .uri(uriBuilder -> uriBuilder.path("/v1/assets/" + symbol + "/params")
                        .queryParam("account_id", accountId).build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(json -> {
                    try {
                        GetAssetParamsResponseDTO dto = objectMapper.readValue(json, GetAssetParamsResponseDTO.class); // Десериализация
                        return Mono.just(ResponseEntity.ok(dto));
                    } catch (Exception ex) {
                        log.error("Error parsing JSON to GetAssetParamsResponseDTO", ex);
                        return Mono.error(ex);
                    }
                });
    }

    /**
     * Метод для получения цепочки опционов для базового актива.
     *
     * @param underlyingSymbol символ базового актива
     * @return объект-обертка с информацией о цепочке опционов
     */
    public Mono<ResponseEntity<OptionsChainResponseDTO>> getOptionsChain(String underlyingSymbol) {
        log.info("Fetching options chain for underlying symbol: {}", underlyingSymbol);
        return authenticatedWebClient.get()
                .uri(uriBuilder -> uriBuilder.path("/v1/assets/" + underlyingSymbol + "/options").build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(json -> {
                    try {
                        OptionsChainResponseDTO dto = objectMapper.readValue(json, OptionsChainResponseDTO.class); // Десериализация
                        return Mono.just(ResponseEntity.ok(dto));
                    } catch (Exception ex) {
                        log.error("Error parsing JSON to OptionsChainResponseDTO", ex);
                        return Mono.error(ex);
                    }
                });
    }

    /**
     * Метод для получения расписания торгов для заданного финансового инструмента.
     *
     * @param symbol символ финансового инструмента
     * @return объект-обертка с расписанием торговли для актива
     */
    public Mono<ResponseEntity<ScheduleResponseDTO>> getSchedule(String symbol) {
        log.info("Fetching trading schedule for symbol: {}", symbol);
        return authenticatedWebClient.get()
                .uri(uriBuilder -> uriBuilder.path("/v1/assets/" + symbol + "/schedule").build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(json -> {
                    try {
                        ScheduleResponseDTO dto = objectMapper.readValue(json, ScheduleResponseDTO.class);
                        return Mono.just(ResponseEntity.ok(dto));
                    } catch (Exception ex) {
                        log.error("Error parsing JSON to ScheduleResponseDTO", ex);
                        return Mono.error(ex);
                    }
                });
    }
}