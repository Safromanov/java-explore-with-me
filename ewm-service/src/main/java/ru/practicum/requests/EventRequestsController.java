package ru.practicum.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.requests.dto.EventRequestDto;

import javax.validation.constraints.Min;
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
    public EventRequestDto postEventRequest(@PathVariable @Min(1) long userId,
                                            @RequestParam @Min(1) long eventId) {
        log.debug("POST /users/{}/requests with eventId: {}.", userId, eventId);
        return eventRequestService.postEventRequest(userId, eventId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventRequestDto> getEventRequests(@PathVariable @Min(1) long userId) {
        log.debug("GET /users/{}/requests.", userId);
        return eventRequestService.getEventRequests(userId);
    }

    @PatchMapping("/{eventRequestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestDto cancelEventRequests(@PathVariable @Min(1) long userId, @PathVariable @Min(1) long eventRequestId) {
        log.debug("PATCH /users/{}/requests/{}/cancel.", userId, eventRequestId);
        return eventRequestService.cancelEventRequests(userId, eventRequestId);
    }
}
