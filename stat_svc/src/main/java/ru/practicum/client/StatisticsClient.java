package ru.practicum.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ru.practicum.model.dto.HitDto;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class StatisticsClient {

    private final RestTemplate rest;

    public StatisticsClient(RestTemplate rest) {
        this.rest = rest;
    }

    public ResponseEntity<Object> getStaticsForUri(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {
        Map<String, Object> parameters = Map.of("start", start, "end", end, "uris", uris, "unique", unique);
        return get("/stats", parameters);
    }

    public ResponseEntity<Object> addHit(String appName, String uri, String ip, LocalDateTime timestamp) {
        HitDto hitDto = new HitDto(appName, uri, ip, timestamp);
        return post("/hit", hitDto);
    }

    private ResponseEntity<Object> get(String path, Map<String, Object> parameters) {
        HttpEntity<Object> requestEntity = new HttpEntity<>(new HttpHeaders());
        ResponseEntity<Object> statServerResponse;
        try {
            statServerResponse = rest
                    .exchange(path, HttpMethod.GET, requestEntity, Object.class, parameters);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareResponse(statServerResponse);
    }

    private <T> ResponseEntity<Object> post(String path, T body) {
        HttpEntity<Object> requestEntity = new HttpEntity<>(body);
        ResponseEntity<Object> statServerResponse;
        try {
            statServerResponse = rest
                    .exchange(path, HttpMethod.POST, requestEntity, Object.class);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareResponse(statServerResponse);
    }

    private static ResponseEntity<Object> prepareResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }
}
