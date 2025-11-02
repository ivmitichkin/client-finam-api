package finam.client.controller;

import finam.client.dto.AssetsResponseDTO;
import finam.client.dto.ClockResponseDTO;
import finam.client.dto.ExchangesResponseDTO;
import finam.client.dto.GetAssetParamsResponseDTO;
import finam.client.dto.GetAssetResponseDTO;
import finam.client.dto.OptionsChainResponseDTO;
import finam.client.dto.ScheduleResponseDTO;
import finam.client.service.DownloadService;
import finam.client.service.ToolsService;
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
 * Контроллер для управления финансовыми инструментами и обработки клиентских запросов.
 */
@RestController
@RequestMapping("/tools")
@AllArgsConstructor
@Tag(name = "Tools", description = "Дополнительные инструменты и сервисы.")
public class ToolsController {

    private final ToolsService toolsService;
    private final DownloadService downloadService;

    /**
     * Метод для получения полного списка активов.
     *
     * @return список активов.
     */
    @Operation(
            summary = "Получение списка активов",
            description = """
                Загружает полный список финансовых инструментов (активов).
                """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список активов успешно возвращён.")
            }
    )
    @GetMapping("/assets")
    public Mono<ResponseEntity<AssetsResponseDTO>> getAssets() {
        return toolsService.getAssets();
    }

    /**
     * Метод для получения текущего серверного времени.
     *
     * @return серверное время.
     */
    @Operation(
            summary = "Получение текущего серверного времени",
            description = """
                Определяет текущее время сервера Finam.
                """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Серверное время получено.")
            }
    )
    @GetMapping("/server_time")
    public Mono<ResponseEntity<ClockResponseDTO>> getServerTime() {
        return toolsService.getServerTime();
    }

    /**
     * Метод для получения конкретной информации о финансовом инструменте.
     *
     * @param symbol   символ актива;
     * @param accountId уникальный идентификатор аккаунта;
     * @return информация о выбранном активе.
     */

    @Operation(
            summary = "Получение информации о финансовом инструменте",
            description = """
                Возвращает информацию о конкретном финансовом инструменте.
                Необходимы тикер инструмента и идентификатор аккаунта.
                Если актив не найден, возвращает ошибку 404.
                """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Информация о выбранном активе предоставлена."),
                    @ApiResponse(responseCode = "404", description = "Актив не найден.")
            }
    )
    @GetMapping(value = "/assets/{symbol}")
    public Mono<ResponseEntity<GetAssetResponseDTO>> getAsset(
            @PathVariable String symbol,
            @RequestParam(name = "account_id") String accountId) {
        return toolsService.getAsset(symbol, accountId);
    }

    /**
     * Метод для получения списка бирж.
     *
     * @return список бирж.
     */
    @Operation(
            summary = "Получение списка бирж",
            description = """
                Возвращает перечень используемых в торговле бирж.
                """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список бирж возвращён.")
            }
    )
    @GetMapping(value = "/exchanges")
    public Mono<ResponseEntity<ExchangesResponseDTO>> getExchanges() {
        return toolsService.getExchanges();
    }

    /**
     * Метод для получения параметров финансового инструмента.
     *
     * @param symbol   символ актива
     * @param accountId уникальный идентификатор аккаунта
     * @return параметры актива
     */
    @Operation(
            summary = "Получение параметров финансового инструмента",
            description = """
                Получает расширенную информацию о финансовом инструменте.
                В качестве параметров используется тикер актива и идентификатор аккаунта.
                Возможна ошибка 404, если актив не найден.
                """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Параметры актива предоставлены."),
                    @ApiResponse(responseCode = "404", description = "Актив не найден.")
            }
    )
    @GetMapping(value = "/assets/{symbol}/params")
    public Mono<ResponseEntity<GetAssetParamsResponseDTO>> getAssetParam(
            @PathVariable String symbol,
            @RequestParam(name = "account_id") String accountId) {
        return toolsService.getAssetParam(symbol, accountId);
    }

    /**
     * Метод для получения цепочки опционов для базового актива.
     *
     * @param underlying_symbol символ базового актива;
     * @return цепочка опционов.
     */
    @Operation(
            summary = "Получение цепочки опционов для базового актива",
            description = """
                Загружает цепочку опционных контрактов для базового актива.
                В параметрах указывается тикер базового актива.
                При отсутствии данных вернется ошибка 404.
                """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Цепочка опционов возвращена."),
                    @ApiResponse(responseCode = "404", description = "Базовый актив не найден.")
            }
    )
    @GetMapping(value = "/assets/{underlying_symbol}/options")
    public Mono<ResponseEntity<OptionsChainResponseDTO>> getOptionsChain(@PathVariable String underlying_symbol) {
        return toolsService.getOptionsChain(underlying_symbol);
    }

    /**
     * Метод для получения расписания торгов для финансового инструмента.
     *
     * @param symbol символ актива;
     * @return расписание торгов.
     */
    @Operation(
            summary = "Расписание торговли инструментом",
            description = """
                Извлекает график торговой сессии для выбранного финансового инструмента.
                В параметрах принимает тикер инструмента.
                Если инструмент не найден, возвращает ошибку 404.
                """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "График торговой сессии предоставлен."),
                    @ApiResponse(responseCode = "404", description = "Финансовый инструмент не найден.")
            }
    )
    @GetMapping(value = "/assets/{symbol}/schedule")
    public Mono<ResponseEntity<ScheduleResponseDTO>> getSchedule(@PathVariable String symbol) {
        return toolsService.getSchedule(symbol);
    }

    /**
     * Точка входа для скачивания файла assets.json
     *
     * @return .json - файл активов.
     */
    @Operation(
            summary = "Загрузка файла активов (.json)",
            description = """
                Доступна точка загрузки файлов, содержащих список активов в формате JSON.
                Вернется кликабельная ссылка (Download file) на загрузку assets.json
                """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Файл загружен успешно.")
            }
    )
    @GetMapping("/assets/file")
    public ResponseEntity<byte[]> downloadAssetsFile() {
        return downloadService.downloadAssets();
    }
}