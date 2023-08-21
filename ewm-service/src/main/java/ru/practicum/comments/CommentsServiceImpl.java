package ru.practicum.comments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comments.dto.CommentDtoResponse;
import ru.practicum.comments.dto.CreateCommentDto;
import ru.practicum.event.EventRepository;
import ru.practicum.event.model.Event;
import ru.practicum.exceptionHandler.NotFoundException;
import ru.practicum.user.UserRepository;
import ru.practicum.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CommentsServiceImpl {

    private final CommentRepository commentRepository;

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    public CommentDtoResponse createComment( Long commenterId, CreateCommentDto createCommentDto) {
        User commenter = userRepository.findById(commenterId).orElseThrow(() ->
                new NotFoundException("User (Commenter) dont found"));
        Event event = eventRepository.findById(createCommentDto.getEvent())
                .orElseThrow(() -> new NotFoundException("Event id " + createCommentDto.getEvent() + " dont found"));

        Comment comment = CommentMapper.toComment(commenter, event, createCommentDto);
        comment = commentRepository.save(comment);
        return CommentMapper.toCommentDtoResponse(comment);
    }

    public CommentDtoResponse updateComment(Long userId, Long eventId, Long commentId, Long commenterId, CreateCommentDto commentDto) {
        Comment comment = commentRepository.findByIdAndAuthorIdAndEventId(commentId, commenterId, eventId)
                .orElseThrow(() -> new NotFoundException("Comment " + commentId + " dont find"));
        comment.setText(commentDto.getText());
        commentRepository.save(comment);
        return CommentMapper.toCommentDtoResponse(comment);
    }

    public List<CommentDtoResponse> getCommentsByParam(Long eventId, int from, int size) {
        eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event dont found"));
        PageRequest pageRequest = getPageRequest(from, size);
        Page<Comment> page = commentRepository.findAllByEventId(eventId, pageRequest);
        return page.get().map(CommentMapper::toCommentDtoResponse).collect(Collectors.toList());
    }

    private PageRequest getPageRequest(int from, int size) {
        return PageRequest.of(from > 0 ? from / size : 0, size);
    }
}
