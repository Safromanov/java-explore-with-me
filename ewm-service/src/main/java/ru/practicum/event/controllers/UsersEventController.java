package ru.practicum.event.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentDtoResponse;
import ru.practicum.comments.dto.CreateCommentDto;
import ru.practicum.event.dto.EventCreateDto;
import ru.practicum.event.dto.EventPatchUserDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.FullEventResponseDto;
import ru.practicum.event.service.EventService;
import ru.practicum.exceptionHandler.BadRequestException;
import ru.practicum.requests.dto.EventRequestsPatchDto;
import ru.practicum.requests.dto.FullRequestsDto;
import ru.practicum.requests.dto.StatusListRequestDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Validated
public class UsersEventController {
    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FullEventResponseDto createEvent(@RequestBody @Valid EventCreateDto eventDto,
                                            @PathVariable Long userId) {
        log.info("Creating event, user id {},  dto: {}.", userId, eventDto);
        if (eventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2)))
            throw new BadRequestException("Field: eventDate. The event must start no later than 2 hours from the current time.");
        FullEventResponseDto event = eventService.createEvent(eventDto, userId);
        log.info("Event created, id = " + event.getId());
        return event;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEvents(@PathVariable Long userId,
                                         @RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "10") @Min(1) int size) {
        log.info("GET /users/{}/events.", userId);
        return eventService.getEventsForUser(userId, from, size);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public FullEventResponseDto getEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("GET /users/{}/events.", userId);
        return eventService.getEventForUser(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public FullEventResponseDto updateEvent(@PathVariable @Positive Long userId,
                                            @PathVariable @Positive Long eventId,
                                            @RequestBody @Valid EventPatchUserDto eventDto) {
        log.info("GET /users/{}/events.", userId);
        if (eventDto.getEventDate() != null)
            if (eventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2)))
                throw new BadRequestException("Field: eventDate. The event must start no later than 2 hours from the current time.");
        return eventService.updateEventForUser(userId, eventId, eventDto);
    }

    @GetMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<FullRequestsDto> getRequestForEvent(@PathVariable @Positive Long userId,
                                                    @PathVariable @Positive Long eventId) {
        log.info("GET /users/{}/events.", userId);
        return eventService.getRequestForEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public StatusListRequestDto changeStatusRequests(@PathVariable @Positive Long userId,
                                                     @PathVariable @Positive Long eventId,
                                                     @RequestBody @Valid EventRequestsPatchDto dto, HttpServletRequest request) {
        log.info("Patch Request {} with dto: {}.", request.getRequestURI(), dto);
        return eventService.changeStatusRequests(userId, eventId, dto);
    }

    @PostMapping("/{eventId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDtoResponse createComment(@PathVariable @Positive Long userId,
                                            @PathVariable @Positive Long eventId,
                                            @RequestParam @Positive Long commenterId,
                                            @RequestBody @Valid CreateCommentDto comment) {
        log.info("User {} Created comment '{}' for event {}", commenterId, comment.getText(), eventId);
        CommentDtoResponse response = eventService.createComment(userId, eventId, commenterId, comment);
        log.info("Comment created, id = " + response.getId());
        return response;
    }

    @PatchMapping("/{eventId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public CommentDtoResponse updateComment(@PathVariable @Positive Long userId,
                                            @PathVariable @Positive Long eventId,
                                            @RequestParam @Positive Long commenterId,
                                            @RequestParam @Positive Long commentId,
                                            @RequestBody @Valid CreateCommentDto comment) {
        log.info("User {} updating comment {} with text {} for event {}", userId, commentId, comment.getText(), eventId);
        return eventService.updateComment(userId, eventId, commentId, commenterId, comment);
    }
}
