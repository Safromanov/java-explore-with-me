package ru.practicum.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.requests.dto.EventRequestDto;

import javax.servlet.http.HttpServletRequest;
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
    public EventRequestDto postEventRequest(@PathVariable long userId,
                                            @RequestParam long eventId, HttpServletRequest request) {
        log.info("POST Event Request  {} with eventId: {}.", request.getRequestURI(), eventId);
        return eventRequestService.postNewEventRequest(userId, eventId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventRequestDto> getEventRequests(@PathVariable @Min(1) long userId) {
        log.info("GET /users/{}/requests.", userId);
        return eventRequestService.getEventRequests(userId);
    }

    @PatchMapping("/{eventRequestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestDto cancelEventRequests(@PathVariable @Min(1) long userId, @PathVariable @Min(1) long eventRequestId) {
        log.info("PATCH /users/{}/requests/{}/cancel.", userId, eventRequestId);
        return eventRequestService.cancelEventRequests(userId, eventRequestId);
    }
}
