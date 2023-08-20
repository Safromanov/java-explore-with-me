package ru.practicum.event.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentDtoResponse;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.FullEventResponseDto;
import ru.practicum.event.model.SortEvent;
import ru.practicum.event.service.EventService;
import ru.practicum.exceptionHandler.BadRequestException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class PublicEventController {

    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEventsByParam(@RequestParam(required = false) String text,
                                                @RequestParam(required = false) Set<Long> categories,
                                                @RequestParam(required = false) Boolean paid,
                                                @RequestParam(required = false) LocalDateTime rangeStart,
                                                @RequestParam(required = false) LocalDateTime rangeEnd,
                                                @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                @RequestParam(required = false) SortEvent sort,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                @RequestParam(defaultValue = "10") @Positive int size,
                                                HttpServletRequest request) {
        log.info("GET /events with params: {}, .",
                request.getQueryString());
        if (rangeStart != null && rangeEnd != null)
            if (rangeStart.isAfter(rangeEnd))
                throw new BadRequestException("Field: eventDate. The event must start no later than the current time.");
        return eventService.getEventsByParam(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public FullEventResponseDto getEvent(@PathVariable @Positive long eventId, HttpServletRequest request) {
        log.info("GET  Event - {}.", request.getRequestURI());
        return eventService.getEventPublic(eventId, request);
    }

    @GetMapping("/{eventId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDtoResponse> getCommentsByParam(@PathVariable @Positive Long eventId,
                                                       @RequestParam(defaultValue = "0") int from,
                                                       @RequestParam(defaultValue = "10") @Min(1) int size) {
        log.info("Get comments event - {} by param", eventId);
        List<CommentDtoResponse> response = eventService.getCommentsByParam(eventId, from, size);
        log.info("Comments size = " + response.size());
        return response;
    }
}
