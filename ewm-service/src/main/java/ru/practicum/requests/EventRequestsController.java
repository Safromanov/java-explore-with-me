package ru.practicum.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.requests.dto.EventRequestDto;

import javax.validation.constraints.Min;

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
        log.debug("POST /users/" + userId + "/requests with eventId: {}.", eventId);
        return eventRequestService.postEventRequest(userId, eventId);
    }


}
