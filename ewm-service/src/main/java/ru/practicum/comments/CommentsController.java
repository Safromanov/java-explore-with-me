package ru.practicum.comments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentDtoResponse;
import ru.practicum.comments.dto.CreateCommentDto;
import ru.practicum.comments.dto.UpdateCommentDto;
import ru.practicum.comments.service.CommentsService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class CommentsController {

    private final CommentsService commentsService;

    @PostMapping("/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDtoResponse createComment(@RequestParam @Positive Long authorId,
                                            @RequestBody @Valid CreateCommentDto comment) {
        log.info("User {} creating comment '{}' for event {}", authorId, comment.getText(), comment.getEvent());
        return commentsService.createComment(authorId, comment);
    }

    @GetMapping("/comments")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDtoResponse> getCommentsByAuthorId(@RequestParam @Positive Long authorId,
                                                          @RequestParam(defaultValue = "0") int from,
                                                          @RequestParam(defaultValue = "10") @Min(1) int size) {
        log.info("Get all comments by author id - {}", authorId);
        return commentsService.getCommentsByAuthor(authorId, from, size);
    }

    @GetMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDtoResponse getCommentById(@PathVariable Long commentId) {
        log.info("Get comment by id - {}", commentId);
        return commentsService.getCommentById(commentId);
    }

    @PatchMapping("/comments")
    @ResponseStatus(HttpStatus.OK)
    public CommentDtoResponse updateComment(@RequestParam @Positive Long authorId,
                                            @RequestBody @Valid UpdateCommentDto comment) {
        log.info("User {} update comment {} - {}", authorId, comment.getId(), comment.getText());
        return commentsService.updateComment(authorId, comment);
    }

    @PatchMapping("/admin/comments")
    @ResponseStatus(HttpStatus.OK)
    public CommentDtoResponse updateCommentByAdmin(@RequestBody @Valid UpdateCommentDto comment) {
        log.info("Admin update comment {} - {}", comment.getId(), comment.getText());
        return commentsService.updateCommentByAdmin(comment);
    }

    @DeleteMapping("/admin/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentByAdmin(@RequestParam @Positive Long commentId) {
        log.info("Delete Comment - {} by Admin", commentId);
        commentsService.deleteCommentByAdmin(commentId);
    }

    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable @Positive Long commentId, @RequestParam @Positive Long authorId) {
        log.info("User {} delete comment {}", authorId, commentId);
        commentsService.deleteComment(authorId, commentId);
    }
}
