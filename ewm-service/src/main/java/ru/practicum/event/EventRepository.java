package ru.practicum.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import ru.practicum.category.model.Category;
import ru.practicum.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("select (count(e) > 0) from Event e where e.category = ?1")
    boolean existsByCategory(Category category);


    Optional<Event> findByInitiatorIdAndId(Long id, Long id1);

    @Query("SELECT e FROM Event e " +
            "RIGHT JOIN  EventRequest r on e.id = r.event.id " +
            "WHERE (:text IS NULL OR e.description like %:text% OR e.annotation like %:text%)  " +
            "AND (:categories IS NULL OR e.category.id in :categories) " +
            "AND (:paid IS NULL OR e.paid = :paid) " +
            "AND (:rangeStart IS NULL OR e.eventDate >= :rangeStart) " +
            "AND (:rangeEnd IS NULL OR e.eventDate <= :rangeEnd) " +
            "AND (r.status = ru.practicum.requests.model.Status.CONFIRMED OR r.status = null) " +
            "AND (e.state = ru.practicum.event.model.State.PUBLISHED) " +
            "GROUP BY e " +
            "HAVING count(r) < e.participantLimit"
    )
    Page<Event> getAvailableEventsByParam(@Param("text") String text,
                                          @Param("categories") Set<Long> categories,
                                          @Param("paid") Boolean paid,
                                          @Param("rangeStart") LocalDateTime rangeStart,
                                          @Param("rangeEnd") LocalDateTime rangeEnd,
                                          Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "WHERE (:text IS NULL OR e.description like %:text% OR e.annotation like %:text%)  " +
            "AND (:categories IS NULL OR e.category.id in :categories) " +
            "AND (:paid IS NULL OR e.paid = :paid) " +
            "AND CAST(COALESCE(:rangeStart, '1970-01-01') as timestamp)  <= e.eventDate  " +
            "AND CAST(COALESCE(:rangeEnd, '3038-01-19')  as timestamp) >= e.eventDate")
    Page<Event> getEventsByParam(@Param("text") String text,
                                 @Param("categories") Set<Long> categories,
                                 @Nullable @Param("paid") Boolean paid,
                                 @Nullable @Param("rangeStart") LocalDateTime rangeStart,
                                 @Nullable @Param("rangeEnd") LocalDateTime rangeEnd,
                                 Pageable pageable);

    @Query("select e from Event e where e.id in ?1")
    List<Event> findByIdIn(Set<Long> ids);

}