package ru.practicum.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.requests.dto.EventRequestDto;
import ru.practicum.requests.service.EventRequestService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@Validated
public class EventRequestsController {

    private final EventRequestService eventRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventRequestDto createEventRequest(@PathVariable @Positive long userId,
                                              @RequestParam @Positive long eventId, HttpServletRequest request) {
        log.info("POST Event Request  {} with eventId: {}.", request.getRequestURI(), eventId);
        return eventRequestService.createEventRequest(userId, eventId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventRequestDto> getEventRequests(@PathVariable @Positive long userId) {
        log.info("GET /users/{}/requests.", userId);
        return eventRequestService.getEventRequests(userId);
    }

    @PatchMapping("/{eventRequestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestDto cancelEventRequests(@PathVariable @Positive long userId, @PathVariable @Positive long eventRequestId) {
        log.info("PATCH /users/{}/requests/{}/cancel.", userId, eventRequestId);
        return eventRequestService.cancelEventRequests(userId, eventRequestId);
    }
}
