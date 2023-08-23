package ru.practicum.comments;

import ru.practicum.comments.dto.CommentDtoResponse;
import ru.practicum.comments.dto.CreateCommentDto;
import ru.practicum.comments.model.Comment;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

public class CommentMapper {

    public static Comment toComment(User author, Event event, CreateCommentDto commentDto) {
        Comment comment = new Comment();
        comment.setCreated(LocalDateTime.now());
        comment.setText(commentDto.getText());
        comment.setEvent(event);
        comment.setAuthor(author);
        comment.setEdited(false);
        return comment;
    }

    public static CommentDtoResponse toCommentDtoResponse(Comment comment) {
        CommentDtoResponse dtoResponse = new CommentDtoResponse();
        dtoResponse.setId(comment.getId());
        dtoResponse.setAuthorName(comment.getAuthor().getName());
        dtoResponse.setCreated(comment.getCreated());
        dtoResponse.setText(comment.getText());
        dtoResponse.setEventId(comment.getEvent().getId());
        dtoResponse.setEdited(comment.isEdited());
        return dtoResponse;
    }
}
