package ru.practicum;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ru.practicum.dto.ClientStatDto;
import ru.practicum.dto.GetStatDto;
import ru.practicum.dto.HitDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsClient {

    private final RestTemplate rest;

    public StatisticsClient(RestTemplate rest) {
        this.rest = rest;
    }

    public List<GetStatDto> getStaticsForUri(ClientStatDto clientStatDto) {
        return (List<GetStatDto>) (get("/stats", clientStatDto.toMap()).getBody());
    }

    public ResponseEntity<Object> addHit(String appName, HttpServletRequest request) {
        HitDto hitDto = new HitDto(appName, request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());
        return post("/hit", hitDto);
    }

    public ResponseEntity<Object> addHitFor(String appName, HttpServletRequest request) {
        HitDto hitDto = new HitDto(appName, request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());
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
