package ru.practicum;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.atlassian.oai.validator.mockmvc.OpenApiValidationMatchers.openApi;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class StatControllerTest {
    @Autowired
    MockMvc mockMvc;


    @Test
    void postStatic_returnResponseWithStatusOk() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.post("/hit");


        this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isCreated(),
                        openApi().isValid("static/ewm-stats-service-spec.json")
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
