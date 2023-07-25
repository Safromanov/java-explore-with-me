package ru.practicum.controller.requests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.model.Event;
import ru.practicum.requests.EventRequestRepository;
import ru.practicum.requests.model.EventRequest;
import ru.practicum.requests.model.Status;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

import static com.atlassian.oai.validator.mockmvc.OpenApiValidationMatchers.openApi;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class EventRequestsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EventRequestRepository eventRequestRepository;

    @Test
    void postEventRequest_returnStatusCreated() throws Exception {

        var requestBuilder = MockMvcRequestBuilders.post("/users/1/requests")
                .param("eventId", "1")
                .contentType(MediaType.APPLICATION_JSON);

        var mvcResult = this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isCreated(),
                        openApi().isValid("static/ewm-main-service-spec.json"),
                        jsonPath("$.created")
                                .value(matchesPattern("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"))
                ).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    @Transactional
    void getEventRequest_returnStatusCreated() throws Exception {
        User user = entityManager.find(User.class, 1L);
        Event event = entityManager.find(Event.class, 1L);

        eventRequestRepository.save(new EventRequest(1L, user, event, LocalDateTime.now(), Status.PENDING));

        var requestBuilder = MockMvcRequestBuilders.get("/users/1/requests")
                .param("eventId", "1")
                .contentType(MediaType.APPLICATION_JSON);

        var mvcResult = this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        openApi().isValid("static/ewm-main-service-spec.json")
                ).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    @Transactional
    void cancelEventRequest_returnStatusOk() throws Exception {
        User user = entityManager.find(User.class, 1L);
        Event event = entityManager.find(Event.class, 1L);

        EventRequest eventRequest = eventRequestRepository.save(new EventRequest(1L, user, event, LocalDateTime.now(), Status.CONFIRMED));

        var requestBuilder = MockMvcRequestBuilders.patch(String.format("/users/%d/requests/%d/cancel", user.getId(), eventRequest.getId()))
                .param("eventId", "1")
                .contentType(MediaType.APPLICATION_JSON);

        var mvcResult = this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        openApi().isValid("static/ewm-main-service-spec.json")
                ).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }
}