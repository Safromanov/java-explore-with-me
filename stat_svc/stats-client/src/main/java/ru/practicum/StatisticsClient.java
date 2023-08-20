package ru.practicum;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
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
@Slf4j
public class StatisticsClient {

    private final RestTemplate rest;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public StatisticsClient(RestTemplate rest) {
        this.rest = rest;
    }

    public List<GetStatDto> getStaticsForUri(ClientStatDto clientStatDto) throws JsonProcessingException {
        Object body = get(clientStatDto.toMap()).getBody();
        return objectMapper.convertValue(body, new TypeReference<>() {
        });
    }

    public ResponseEntity<Object> addHit(String appName, String requestURI, HttpServletRequest request) {
        HitDto hitDto = new HitDto(appName, requestURI, request.getRemoteAddr(), LocalDateTime.now());
        return post("/hit", hitDto);
    }

    @SneakyThrows
    private ResponseEntity<Object> get(Map<String, Object> parameters) {
        HttpEntity<Object> requestEntity = new HttpEntity<>(null, defaultHeaders());
        ResponseEntity<Object> statServerResponse;
        parameters.put("start", LocalDateTime.now().minusYears(3));
        try {
            statServerResponse = rest.exchange("/stats?uris={uris}&unique=true", HttpMethod.GET, requestEntity, Object.class, parameters);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
        return prepareResponse(statServerResponse);
    }

    private <T> ResponseEntity<Object> post(String path, T map) {
        HttpEntity<Object> requestEntity = new HttpEntity<>(map, defaultHeaders());
        ResponseEntity<Object> statServerResponse;
        try {
            statServerResponse = rest.exchange(path, HttpMethod.POST, requestEntity, Object.class);
            log.info(statServerResponse.toString());
        } catch (HttpStatusCodeException e) {
            log.info(e.getResponseBodyAsString());
            e.printStackTrace();
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

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
