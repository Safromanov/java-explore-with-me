package ru.practicum.comments;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.comments.model.Comment;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByIdAndAuthorId(Long commentId, Long commenterId);

    long countByEventId(Long id);

    Page<Comment> findAllByAuthorId(Long eventId, Pageable pageable);

}