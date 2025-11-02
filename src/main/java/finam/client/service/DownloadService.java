package finam.client.service;

import finam.client.component.DateConverter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.OffsetDateTime;

/**
 * Сервис для загрузки активов с API Finam.
 */
@Service
@Slf4j
@AllArgsConstructor
public class DownloadService {

    private final OkHttpClient client;
    private final DateConverter dateConverter;

    private static final String ASSETS_URL = "https://api.finam.ru/v1/assets";

    /**
     * Метод для скачивания списка активов в формате JSON.
     *
     * @return объект ResponseEntity с байтами файла и заголовком Content-Disposition,
     *          позволяющим скачать файл с именем assets-дата.json.
     */
    public ResponseEntity<byte[]> downloadAssets() {
        try {
            Request request = new Request.Builder()
                    .url(ASSETS_URL)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.body() == null || !response.isSuccessful()) {
                    throw new IOException("Missing body or error in data retrieval.");
                }

                String formattedDate = dateConverter.convertFromServerFormat(OffsetDateTime.now().toString());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"assets-" + formattedDate + ".json\"");

                return new ResponseEntity<>(response.body().bytes(), headers, HttpStatus.OK);
            }
        } catch (IOException ex) {
            log.error("Error during the request execution: {}", ex.getMessage(), ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while downloading file.", ex);
        }
    }
}