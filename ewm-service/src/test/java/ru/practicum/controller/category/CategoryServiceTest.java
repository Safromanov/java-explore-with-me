package ru.practicum.controller.category;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.atlassian.oai.validator.mockmvc.OpenApiValidationMatchers.openApi;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CategoryServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getCategories_returnStatusOk() throws Exception {

        var requestBuilder = MockMvcRequestBuilders.get("/categories").contentType(MediaType.APPLICATION_JSON);

        var mvcResult = mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        openApi().isValid("static/ewm-main-service-spec.json")
                ).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void getCategory_returnStatusOk() throws Exception {

        var requestBuilder = MockMvcRequestBuilders.get("/categories/1").contentType(MediaType.APPLICATION_JSON);

        var mvcResult = mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        openApi().isValid("static/ewm-main-service-spec.json")
                ).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

}