package ru.practicum.requests;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EventRequestRepository extends JpaRepository<EventRequest, Long> {
    @Query("select count(e) from EventRequest e where e.event.id = ?1 and e.status = 'CONFIRMED'")
    int countByStatusConfirmed(long eventId);


}