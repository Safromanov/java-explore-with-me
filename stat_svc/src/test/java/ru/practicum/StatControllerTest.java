package ru.practicum;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
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

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.ArgumentMatchers.*;
import static com.atlassian.oai.validator.mockmvc.OpenApiValidationMatchers.openApi;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class StatControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void postStatic_returnResponseWithStatusOk() throws Exception {

        PostDtoStatReq dtoStatReq = new PostDtoStatReq("ewm-main-service",
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
                   //     content().json(json)

                        //        content().contentTypeCompatibleWith(""),
//                        content().json("""
//                                {
//                                  "app": "ewm-main-service",
//                                  "uri": "/events/1",
//                                  "ip": "192.163.0.1",
//                                  "timestamp": "2022-09-06 11:00:23"
//                                }
//                                """
                );
    }
}
