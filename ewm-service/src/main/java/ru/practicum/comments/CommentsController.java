package ru.practicum.comments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentDtoResponse;
import ru.practicum.comments.dto.CreateCommentDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentsController {

    private final CommentsServiceImpl commentsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDtoResponse createComment(@RequestParam @Positive Long commenterId,
                                            @RequestBody @Valid CreateCommentDto comment) {
        log.info("User {} Created comment '{}' for event {}", commenterId, comment.getText(), comment.getEvent());
        CommentDtoResponse response = commentsService.createComment(commenterId, comment);
        log.info("Comment created, id = " + response.getId());
        return response;
    }

//    @PostMapping("/{commentId}")
//    @ResponseStatus(HttpStatus.CREATED)
//    public CommentDtoResponse getComment(@PathVariable Long commentId,
//                                            @RequestParam @Positive Long commenterId,
//                                            @RequestBody @Valid CreateCommentDto comment) {
//        log.info("User {} Created comment '{}' for event {}", commenterId, comment.getText(), eventId);
//        CommentDtoResponse response = commentsService.createComment(commenterId, comment);
//        log.info("Comment created, id = " + response.getId());
//        return response;
//    }

//    @PatchMapping("/{eventId}/comments")
//    @ResponseStatus(HttpStatus.OK)
//    public CommentDtoResponse updateComment(@PathVariable @Positive Long userId,
//                                            @PathVariable @Positive Long eventId,
//                                            @RequestParam @Positive Long commenterId,
//                                            @RequestParam @Positive Long commentId,
//                                            @RequestBody @Valid CreateCommentDto comment) {
//        log.info("User {} updating comment {} with text {} for event {}", userId, commentId, comment.getText(), eventId);
//        return commentsService.updateComment(userId, eventId, commentId, commenterId, comment);
//    }


}
