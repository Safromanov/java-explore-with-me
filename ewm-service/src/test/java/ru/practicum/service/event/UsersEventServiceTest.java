package ru.practicum.service.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.event.controllers.usersAPI.UsersEventService;
import ru.practicum.requests.dto.EventRequestsPatchDto;
import ru.practicum.requests.model.Status;

import java.util.Collections;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UsersEventServiceTest {
    private final UsersEventService eventService;
    private final ObjectMapper objectMapper;

    @Test
    void patchRequests() throws JsonProcessingException {
        EventRequestsPatchDto patchDto = new EventRequestsPatchDto(Collections.singleton(2L), Status.CONFIRMED);
        System.out.println(objectMapper.writeValueAsString(eventService.patchRequests(1L, 1L, patchDto)));
    }
}
