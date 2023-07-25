package ru.practicum.controller.event;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static com.atlassian.oai.validator.mockmvc.OpenApiValidationMatchers.openApi;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureTestEntityManager
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class EventControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Test
    void getEventsByParam_returnStatusOk() throws Exception {

        var requestBuilder = MockMvcRequestBuilders.get("/events").contentType(MediaType.APPLICATION_JSON)
                .param("categories", "1")
                .param("paid", "true")
                .param("rangeStart", "2020-09-13 21:00:00");
        var mvcResult = this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        openApi().isValid("static/ewm-main-service-spec.json")
                ).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void getEvent_returnStatusOk() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.get("/events/1").contentType(MediaType.APPLICATION_JSON);
        var mvcResult = this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        openApi().isValid("static/ewm-main-service-spec.json")
                ).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }
}