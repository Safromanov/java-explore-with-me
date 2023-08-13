package ru.practicum;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.HitDto;

import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest
@Slf4j
public class ClientTest {
    private final RestTemplate rest;

//    private final ObjectMapper objectMapper =new ObjectMapper()
//            .registerModule(new JavaTimeModule().addDeserializer(LocalDateTime.class,
//                    new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))))
//            .registerModule(new JavaTimeModule().addSerializer(LocalDateTime.class,
//                    new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));

    public ClientTest(@Autowired RestTemplateBuilder builder) {
        this.rest =builder
                .uriTemplateHandler(new DefaultUriBuilderFactory("http://localhost:9090"))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();;
    }

    @Test
    public void addHitTest() throws URISyntaxException, JsonProcessingException {
        HitDto hitDto = new HitDto("appName", "/requestURI", "1.1.1.1", LocalDateTime.now());
//        MultiValueMap<String,String> map = new LinkedMultiValueMap<>() {};
//
//        map.add("app", "appName");
//        map.add("uri", "/requestURI");
//        map.add("ip", "1.1.1.1");
//        map.add("timestamp",  LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

//        String hitDtoJson = objectMapper.writeValueAsString(hitDto);
//        log.info("Sending HitDto: {}", hitDtoJson);
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

