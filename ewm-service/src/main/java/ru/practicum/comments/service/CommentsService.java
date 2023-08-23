package ru.practicum.comments.service;

import ru.practicum.comments.dto.CommentDtoResponse;
import ru.practicum.comments.dto.CreateCommentDto;
import ru.practicum.comments.dto.UpdateCommentDto;

import java.util.List;

public interface CommentsService {
    CommentDtoResponse createComment(Long authorId, CreateCommentDto createCommentDto);

    CommentDtoResponse updateComment(Long authorId, UpdateCommentDto commentDto);

    List<CommentDtoResponse> getCommentsByAuthor(Long authorId, int from, int size);

    void deleteComment(Long authorId, Long commentId);

    CommentDtoResponse getCommentById(Long commentId);

    CommentDtoResponse updateCommentByAdmin(UpdateCommentDto commentDto);

    void deleteCommentByAdmin(Long commentId);
}
