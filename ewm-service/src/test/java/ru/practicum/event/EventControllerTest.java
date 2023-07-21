package ru.practicum.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.category.Category;
import ru.practicum.category.CategoryRepository;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.View;
import ru.practicum.event.model.Action;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.Location;
import ru.practicum.event.model.State;
import ru.practicum.user.model.User;
import ru.practicum.user.model.UserRepository;

import static com.atlassian.oai.validator.mockmvc.OpenApiValidationMatchers.openApi;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class EventControllerTest {

    Category category;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ObjectMapper objectMapper;
    private final EasyRandom easyRandom = new EasyRandom();

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setId(1L);
        user.setName("testUser");
        user.setEmail("test@user.net");
        userRepository.save(user);

        category = new Category();
        category.setName("testCategory");
        category = categoryRepository.save(category);

        Event event = easyRandom.nextObject(Event.class);
        Location location = new Location();
        location.setLon(0.121F);
        location.setLat(0.1212F);
        event.setId(1L);
        event.setInitiator(user);
        event.setCategory(category);
        event.setLocation(location);
        event.setState(State.PENDING);
        eventRepository.save(event);
        event.setId(2L);
        eventRepository.save(event);
    }

    @Test
    void postEvent_returnStatusCreate() throws Exception {
        EventDto eventDto = easyRandom.nextObject(EventDto.class);
        eventDto.setDescription("Test_test_test_test_test_test_test_test_test_test");
        eventDto.setCategory(category.getId());
        var requestBuilder = MockMvcRequestBuilders.post("/users/1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writerWithView(View.Post.class).writeValueAsString(eventDto));
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
        EventDto eventDto = easyRandom.nextObject(EventDto.class);
        eventDto.setDescription("update update update update update update update update update update");
        eventDto.setCategory(1);
        eventDto.setStateAction(Action.SEND_TO_REVIEW);
        var requestBuilder = MockMvcRequestBuilders.patch("/users/1/events/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDto));
        var mvcResult = this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        openApi().isValid("static/ewm-main-service-spec.json")
                ).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }


}