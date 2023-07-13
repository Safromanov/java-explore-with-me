package ru.practicum.server;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.model.dto.HitDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.atlassian.oai.validator.mockmvc.OpenApiValidationMatchers.openApi;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class StatControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void postStat_returnResponseWithStatusCreate() throws Exception {

        HitDto dtoStatReq = new HitDto("ewm-main-service",
                "/events/1",
                "192.163.0.1",
                LocalDateTime.now());

        String json = new ObjectMapper()
                .registerModule(new JavaTimeModule().addDeserializer(LocalDateTime.class,
                        new LocalDateTimeDeserializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .writeValueAsString(dtoStatReq);

        var requestBuilder = MockMvcRequestBuilders.post("/hit").contentType(MediaType.APPLICATION_JSON).content(json);
        this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isCreated(),
                        openApi().isValid("static/ewm-stats-service-spec.json")
                );
    }

    @Test
    void postStat_returnErrorCode() throws Exception {

        HitDto dtoStatReq = new HitDto("ewm-main-service",
                " ",
                "192.163.0.1/l",
                LocalDateTime.now());

        String jsonWithEmptyURI = new ObjectMapper()
                .registerModule(new JavaTimeModule().addDeserializer(LocalDateTime.class,
                        new LocalDateTimeDeserializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .writeValueAsString(new HitDto("ewm-main-service",
                        " ",
                        "192.163.0.1/l",
                        LocalDateTime.now()));

        var requestBuilder = MockMvcRequestBuilders.post("/hit").contentType(MediaType.APPLICATION_JSON).content(jsonWithEmptyURI);

        this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isBadRequest()
                );


        String jsonWithBadIp = new ObjectMapper()
                .registerModule(new JavaTimeModule().addDeserializer(LocalDateTime.class,
                        new LocalDateTimeDeserializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .writeValueAsString(new HitDto("ewm-main-service",
                        "/events/1",
                        "192.163.0.1/l",
                        LocalDateTime.now()));

        requestBuilder= MockMvcRequestBuilders.post("/hit").contentType(MediaType.APPLICATION_JSON).content(jsonWithEmptyURI);
        this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isBadRequest()
                );
    }

    @Test
    void getStat_returnResponseWithStatusOk() throws Exception {
        HitDto dtoStatReq = new HitDto("ewm-main-service",
                "/events/1",
                "192.163.0.1",
                LocalDateTime.now());

        String json = new ObjectMapper()
                .registerModule(new JavaTimeModule().addDeserializer(LocalDateTime.class,
                        new LocalDateTimeDeserializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .writeValueAsString(dtoStatReq);

        var requestBuilder = MockMvcRequestBuilders.post("/hit").contentType(MediaType.APPLICATION_JSON).content(json);
        this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isCreated(),
                        openApi().isValid("static/ewm-stats-service-spec.json")
                );
    }
}
