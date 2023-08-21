package ru.practicum.comments;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByIdAndAuthorIdAndEventId(Long commentId, Long commenterId, Long eventId);

    Optional<Comment> findByEventIdAndId(Long commentId, Long eventId);

    Page<Comment> findAllByEventId(Long eventId, Pageable pageable);

}