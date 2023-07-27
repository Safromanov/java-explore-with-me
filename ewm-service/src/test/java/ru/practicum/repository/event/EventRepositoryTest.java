package ru.practicum.repository.event;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.TestConfig;
import ru.practicum.event.EventRepository;
import ru.practicum.event.model.Event;
import ru.practicum.requests.model.EventRequest;
import ru.practicum.requests.model.Status;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Import(TestConfig.class)
class EventRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EventRepository eventRepository;

    private final String findWord = "FIND_ME";
    private final LocalDateTime now = LocalDateTime.now();
    private final Pageable pageable = PageRequest.of(0, 10);

//    @Test
//    void getEventsByParam() {
//        List<Event> eventsWithAllParam = eventRepository.getEventsByParam(findWord, Collections.singleton(1L), true,
//                now.minusWeeks(3), now.plusMonths(2), pageable);
//
//        List<Event> eventsWithEmptyParam = eventRepository.getEventsByParam(null, null, null,
//                null, null, pageable);
//
//        Event event1 = entityManager.find(Event.class, 1L);
//
//        assertEquals(1, eventsWithAllParam.size(), "The size of the list does not match the expected value");
//        assertEquals(event1.getId(), eventsWithAllParam.get(0).getId());
//
//        assertEquals(2, eventsWithEmptyParam.size(), "The size of the list does not match the expected value");
//    }

    @Test
    void getAvailableEventsByParam() {
        User requester = entityManager.find(User.class, 1L);
        Event event = entityManager.find(Event.class, 1L);

        EventRequest eventRequest = new EventRequest(1L, requester, event, now, Status.CONFIRMED);
        entityManager.merge(eventRequest);
        entityManager.flush();

        List<Event> availableEventsByParam = eventRepository.getAvailableEventsByParam(null, null, null,
                null, null, pageable);

        Event event2 = entityManager.find(Event.class, 2L);

//        assertEquals(1, availableEventsByParam.size(), "The size of the list does not match the expected value");
//        assertEquals(event2.getId(), availableEventsByParam.get(0).getId());
    }

}