package ru.practicum.comments.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comments.CommentMapper;
import ru.practicum.comments.CommentRepository;
import ru.practicum.comments.dto.CommentDtoResponse;
import ru.practicum.comments.dto.CreateCommentDto;
import ru.practicum.comments.dto.UpdateCommentDto;
import ru.practicum.comments.model.Comment;
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
public class CommentsServiceImpl implements CommentsService {

    private final CommentRepository commentRepository;

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    @Override
    public CommentDtoResponse createComment(Long authorId, CreateCommentDto createCommentDto) {
        User author = userRepository.findById(authorId).orElseThrow(() ->
                new NotFoundException("User (Commenter) dont found"));
        Event event = eventRepository.findById(createCommentDto.getEvent())
                .orElseThrow(() -> new NotFoundException("Event id " + createCommentDto.getEvent() + " dont found"));
        Comment comment = CommentMapper.toComment(author, event, createCommentDto);
        comment = commentRepository.save(comment);
        return CommentMapper.toCommentDtoResponse(comment);
    }

    @Override
    public CommentDtoResponse updateComment(Long authorId, UpdateCommentDto commentDto) {
        Comment comment = commentRepository.findByIdAndAuthorId(commentDto.getId(), authorId)
                .orElseThrow(() -> new NotFoundException("Comment " + commentDto.getId() + " dont find"));
        comment.setText(commentDto.getText());
        comment.setEdited(true);
        commentRepository.save(comment);
        return CommentMapper.toCommentDtoResponse(comment);
    }

    private PageRequest getPageRequest(int from, int size) {
        return PageRequest.of(from > 0 ? from / size : 0, size);
    }

    @Override
    public List<CommentDtoResponse> getCommentsByAuthor(Long authorId, int from, int size) {
        User commenter = userRepository.findById(authorId).orElseThrow(() ->
                new NotFoundException("User (Commenter)-" + authorId + " dont found"));
        PageRequest pageRequest = getPageRequest(from, size);
        Page<Comment> page = commentRepository.findAllByAuthorId(authorId, pageRequest);
        return page.get().map(CommentMapper::toCommentDtoResponse).collect(Collectors.toList());
    }

    @Override
    public void deleteComment(Long authorId, Long commentId) {
        commentRepository.findByIdAndAuthorId(commentId, authorId)
                .ifPresentOrElse(commentRepository::delete,
                        () -> {
                            throw new NotFoundException("Comment " + commentId + " dont find");
                        });
    }

    @Override
    public CommentDtoResponse getCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment " + commentId + " dont find"));
        return CommentMapper.toCommentDtoResponse(comment);
    }

    @Override
    public CommentDtoResponse updateCommentByAdmin(UpdateCommentDto commentDto) {
        Comment comment = commentRepository.findById(commentDto.getId())
                .orElseThrow(() -> new NotFoundException("Comment " + commentDto.getId() + " dont find"));
        comment.setEdited(true);
        comment.setText(commentDto.getText());
        commentRepository.save(comment);
        return CommentMapper.toCommentDtoResponse(comment);
    }

    @Override
    public void deleteCommentByAdmin(Long commentId) {
        commentRepository.findById(commentId)
                .ifPresentOrElse(commentRepository::delete,
                        () -> {
                            throw new NotFoundException("Comment " + commentId + " dont find");
                        });
    }
}
