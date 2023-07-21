package ru.practicum.requests;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.requests.dto.FullRequestsDto;

import java.util.List;

public interface EventRequestRepository extends JpaRepository<EventRequest, Long> {
    @Query("select count(e) from EventRequest e where e.event.id = ?1 and e.status = 'CONFIRMED'")
    int countByStatusConfirmed(long eventId);

    List<FullRequestsDto> findAllByEvent_InitiatorIdAndEventId(long initiatorId, long eventId);

    @Query("select e from EventRequest e where e.requester.id = ?1 and e.event.id = ?2 and e.id in ?3")
    List<EventRequest> findByIdIn(long initiatorId, long eventId, long[] requestIds);

    @Query("select e from EventRequest e where e.requester.id = ?1 and e.event.id = ?2 and e.id in ?3")
    List<FullRequestsDto> findDtoByIdIn(long initiatorId, long eventId, long[] requestIds);


}