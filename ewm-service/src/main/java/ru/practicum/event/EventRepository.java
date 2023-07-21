package ru.practicum.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.event.model.Event;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("select e from Event e where e.initiator.id = ?1 and e.id = ?2")
    Optional<Event> findByInitiatorIdAndId(Long initiatorId, Long eventId);
}