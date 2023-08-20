package ru.practicum.requests;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.requests.dto.FullRequestsDto;
import ru.practicum.requests.model.EventRequest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRequestRepository extends JpaRepository<EventRequest, Long> {

    Optional<EventRequest> findByRequesterIdAndEventId(long requesterId, long eventId);

    List<EventRequest> findByRequesterId(Long id);

    Optional<EventRequest> findByRequesterIdAndId(long requesterId, long eventRequestId);

    @Query("select count(e.id) from EventRequest e where e.event.id = ?1 and e.status = ru.practicum.requests.model.Status.CONFIRMED")
    int countByStatusConfirmed(long eventId);

    List<FullRequestsDto> findAllByEvent_InitiatorIdAndEventId(long initiatorId, long eventId);

    @Query("select e from EventRequest e " +
            "left join EventRequest r on e.id = r.event.id " +
            "where e.event.id = ?1 and e.id in ?2 ")
    List<EventRequest> findByIdIn(long eventId, Set<Long> requestIds);

    @Query("select e from EventRequest e where e.event.id = ?1 and e.id in ?2 " +
            "and e.status = ru.practicum.requests.model.Status.CONFIRMED")
    List<FullRequestsDto> findConfirmDtoByIdIn(long eventId, Set<Long> requestIds);

    @Query("select e from EventRequest e where e.event.id = ?1 and e.id in ?2 " +
            "and e.status = ru.practicum.requests.model.Status.REJECTED ")
    List<FullRequestsDto> findRejectedDtoByIdIn(long eventId, Set<Long> requestIds);
}