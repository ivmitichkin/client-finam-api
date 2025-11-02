package finam.client.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Компонент преобразования дат между разными форматами.
 * Используется для конвертации строковых представлений дат при взаимодействии через сторонний API.
 */
@Component
@Slf4j
public class DateConverter {

    private static final String HUMAN_READABLE_FORMAT = "yyyy-MM-dd";
    private static final String SERVER_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSX";

    /**
     * Метод преобразует строку даты из удобочитаемого формата в формат, подходящий для передачи на сервер.
     *
     * @param humanReadableDateString Строка даты в удобочитаемом формате ("yyyy-MM-dd")
     * @return Дата в формате, используемом сервером ("yyyy-MM-dd'T'HH:mm:ss.SSX") или "" пустая строка в случае ошибки.
     */
    public String convertToServerFormat(String humanReadableDateString) {
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(humanReadableDateString + "T00:00:00");
            OffsetDateTime offsetDateTime = localDateTime.atOffset(ZoneOffset.UTC);

            String formattedDate = offsetDateTime.format(DateTimeFormatter.ofPattern(SERVER_DATETIME_FORMAT));
            log.debug("Converted date '{}' into server format: {}", humanReadableDateString, formattedDate);
            return formattedDate;
        } catch (Exception e) {
            log.error("Error converting date to server format", e);
            return "";
        }
    }

    /**
     * Метод преобразует строку даты из формата сервера обратно в удобочитаемую форму.
     *
     * @param serverDateTimeString Строка даты в формате сервера ("yyyy-MM-dd'T'HH:mm:ss.SSX")
     * @return Дата в удобочитаемом формате ("yyyy-MM-dd") или "" пустая строка в случае ошибки.
     */
    public String convertFromServerFormat(String serverDateTimeString) {
        try {
            OffsetDateTime offsetDateTime = OffsetDateTime.parse(serverDateTimeString);
            LocalDateTime localDateTime = offsetDateTime.toLocalDateTime();

            String formattedDate = localDateTime.format(DateTimeFormatter.ofPattern(HUMAN_READABLE_FORMAT));
            log.debug("Converted server date '{}' back to human-readable format: {}", serverDateTimeString, formattedDate);
            return formattedDate;
        } catch (Exception e) {
            log.error("Error converting date from server format", e);
            return "";
        }
    }

}
