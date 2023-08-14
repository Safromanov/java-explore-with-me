package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.HitDto;

import java.time.LocalDateTime;

@SpringBootTest
@Slf4j
public class ClientTest {
    private final RestTemplate rest;

    public ClientTest(@Autowired RestTemplateBuilder builder) {
        this.rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory("http://localhost:9090"))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
        ;
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

    @Test
    @Disabled
    public void addHitTest() {
        HitDto hitDto = new HitDto("appName", "/requestURI", "1.1.1.1", LocalDateTime.now());
        log.info(post("/hit", hitDto).toString());
    }

    private <T> ResponseEntity<Object> post(String path, T map) {

        HttpEntity<Object> requestEntity = new HttpEntity<>(map);
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
}

