package ru.practicum.controller.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jeasy.random.EasyRandom;
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
import ru.practicum.category.Category;
import ru.practicum.event.dto.EventCreateDto;
import ru.practicum.event.dto.View;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.StateUserAction;
import ru.practicum.requests.EventRequestRepository;
import ru.practicum.requests.dto.EventRequestsPatchDto;
import ru.practicum.requests.model.EventRequest;
import ru.practicum.requests.model.Status;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;

import static com.atlassian.oai.validator.mockmvc.OpenApiValidationMatchers.openApi;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UsersPublicEventControllerTest {


    private final EasyRandom easyRandom = new EasyRandom();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private EventRequestRepository eventRequestRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    @Transactional
    void postEvent_returnStatusCreate() throws Exception {
        Category category = entityManager.find(Category.class, 1L);
        EventCreateDto eventDto = easyRandom.nextObject(EventCreateDto.class);
        eventDto.setDescription("Test_test_test_test_test_test_test_test_test_test");
        eventDto.setCategory(category.getId());
        var requestBuilder = MockMvcRequestBuilders.post("/users/1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writerWithView(View.Create.class).writeValueAsString(eventDto));
        var mvcResult = this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isCreated(),
                        openApi().isValid("static/ewm-main-service-spec.json")
                ).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void getEvents_returnStatusOK() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.get("/users/1/events").contentType(MediaType.APPLICATION_JSON);
        var mvcResult = this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        openApi().isValid("static/ewm-main-service-spec.json")
                ).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void getEvent_returnStatusOK() throws Exception {

        var requestBuilder = MockMvcRequestBuilders.get("/users/1/events/1").contentType(MediaType.APPLICATION_JSON);
        var mvcResult = this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        openApi().isValid("static/ewm-main-service-spec.json")
                ).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void patchEvent_returnStatusOK() throws Exception {

        EventCreateDto eventDto = easyRandom.nextObject(EventCreateDto.class);
        eventDto.setDescription("update update update update update update update update update update");
        eventDto.setCategory(1);
        eventDto.setStateAction(StateUserAction.SEND_TO_REVIEW);
        var requestBuilder = MockMvcRequestBuilders.patch("/users/1/events/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDto));
        var mvcResult = this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        openApi().isValid("static/ewm-main-service-spec.json")
                ).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }


    @Test
    @Transactional
    void getRequests() throws Exception {
        User user = entityManager.find(User.class, 1L);
        Event event = entityManager.find(Event.class, 1L);

        EventRequest eventRequest = new EventRequest(1L, user, event, LocalDateTime.now().minusDays(1), Status.PENDING);
        eventRequestRepository.save(eventRequest);

        var requestBuilder = MockMvcRequestBuilders.get("/users/1/events/1/requests")
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
    void patchRequests() throws Exception {
        User user = entityManager.find(User.class, 1L);
        Event event = entityManager.find(Event.class, 1L);
        EventRequest eventRequest = new EventRequest(3L, user, event, LocalDateTime.now().minusDays(1), Status.PENDING);
        eventRequest = eventRequestRepository.save(eventRequest);

        EventRequestsPatchDto patchDto = new EventRequestsPatchDto(Collections.singleton(eventRequest.getId()), Status.CONFIRMED);

        var requestBuilder = MockMvcRequestBuilders.patch(String.format("/users/%d/events/1/requests", user.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patchDto));
        var mvcResult = this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        openApi().isValid("static/ewm-main-service-spec.json")
                ).andReturn();
        System.out.println(mvcResult.getRequest().getRequestURI());
        System.out.println(mvcResult.getResponse().getContentAsString());
    }


    @Test
    void getEvent_returnStatusNotFound() throws Exception {

        var requestBuilder = MockMvcRequestBuilders.get("/users/1/events/12").contentType(MediaType.APPLICATION_JSON);
        var mvcResult = this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isNotFound(),
                        openApi().isValid("static/ewm-main-service-spec.json")
                ).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }


}