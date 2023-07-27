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
import ru.practicum.dto.HitDto;

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
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule()
                    .addDeserializer(LocalDateTime.class,
                            new LocalDateTimeDeserializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));

    @Test
    void postStat_returnResponseWithStatusCreate() throws Exception {

        HitDto dtoStatReq = new HitDto("ewm-main-service",
                "/events/1",
                "192.163.0.1",
                LocalDateTime.now());

        String json = objectMapper.writeValueAsString(dtoStatReq);

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

        String jsonWithEmptyURI = objectMapper
                .writeValueAsString(new HitDto("ewm-main-service",
                        " ",
                        "192.163.0.1/l",
                        LocalDateTime.now()));

        var requestBuilder = MockMvcRequestBuilders.post("/hit").contentType(MediaType.APPLICATION_JSON).content(jsonWithEmptyURI);

        this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isBadRequest()
                );


        String jsonWithBadIp = objectMapper
                .writeValueAsString(new HitDto("ewm-main-service",
                        "/events/1",
                        "192.163.0.1/l",
                        LocalDateTime.now()));

        requestBuilder = MockMvcRequestBuilders.post("/hit")
                .contentType(MediaType.APPLICATION_JSON).content(jsonWithEmptyURI);
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

        String json = objectMapper.writeValueAsString(dtoStatReq);


        var requestBuilder = MockMvcRequestBuilders.post("/hit").contentType(MediaType.APPLICATION_JSON).content(json);
        this.mockMvc.perform(requestBuilder);

        requestBuilder = MockMvcRequestBuilders.get("/stats").contentType(MediaType.APPLICATION_JSON)
                .param("start", LocalDateTime.now().minusDays(1).format(formatter))
                .param("end", LocalDateTime.now().plusMonths(1).format(formatter))
                .param("uris", objectMapper.writeValueAsString(new String[]{"/events/1"}))
                .param("unique", "false");
        System.out.println(requestBuilder);
        var mvcResult = this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        openApi().isValid("static/ewm-stats-service-spec.json")
                )
                .andReturn();
        System.out.println(objectMapper.writeValueAsString(mvcResult.getRequest().getParameterMap()));
    }
}
