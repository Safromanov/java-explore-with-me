package ru.practicum.controller.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.category.dto.RequestCategoryDto;

import static com.atlassian.oai.validator.mockmvc.OpenApiValidationMatchers.openApi;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AdminAdminPublicCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void postCategory_returnStatusCreate() throws Exception {

        RequestCategoryDto requestCategoryDto = new RequestCategoryDto();
        requestCategoryDto.setName("Концерты");

        var requestBuilder = MockMvcRequestBuilders.post("/admin/categories").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestCategoryDto));

        this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isCreated(),
                        openApi().isValid("static/ewm-main-service-spec.json")
                );
    }

    @Test
    void patchCategory_returnStatusOk() throws Exception {

        RequestCategoryDto requestCategoryDto = new RequestCategoryDto();
        requestCategoryDto.setName("Концерты");

        var requestBuilder = MockMvcRequestBuilders.patch("/admin/categories/1").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestCategoryDto));

        this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        openApi().isValid("static/ewm-main-service-spec.json")
                );
    }

    @Test
    void deleteCategory_returnStatusNContent() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.delete("/admin/categories/3")
                .contentType(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isNoContent(),
                        openApi().isValid("static/ewm-main-service-spec.json")
                );
    }
}
