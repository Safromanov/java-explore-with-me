package ru.practicum.requests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.TestConfig;
import ru.practicum.requests.model.EventRequest;

import java.util.Collections;
import java.util.List;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Import(TestConfig.class)
class EventRequestRepositoryTest {

    @Autowired
    private EventRequestRepository eventRequestRepository;

    @Test
    void findByIdIn() {
        List<EventRequest> eventRequests = eventRequestRepository.findByIdIn(1, Collections.singleton(1L));
        System.out.println(eventRequests.size());
    }
}