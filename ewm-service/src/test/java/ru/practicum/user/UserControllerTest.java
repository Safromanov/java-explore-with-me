package ru.practicum.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.user.model.User;
import ru.practicum.user.model.UserRepository;
import ru.practicum.user.model.dto.CreateUserDto;

import static com.atlassian.oai.validator.mockmvc.OpenApiValidationMatchers.openApi;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setName("testUser");
        user.setEmail("test@User.net");
        userRepository.save(user);
    }

    @Test
    void getUsers() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.get("/admin/users").contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        openApi().isValid("static/ewm-main-service-spec.json")
                );
    }

    @Test
    void postUser() throws Exception {

        CreateUserDto createUserDto = new CreateUserDto("abc", "ayf@be.cu");

        var requestBuilder = MockMvcRequestBuilders.post("/admin/users").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(createUserDto));

        this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isCreated(),
                        openApi().isValid("static/ewm-main-service-spec.json")
                );
    }

    @Test
    void deleteUser() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.delete("/admin/users/1").contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isNoContent(),
                        openApi().isValid("static/ewm-main-service-spec.json")
                );
    }
}