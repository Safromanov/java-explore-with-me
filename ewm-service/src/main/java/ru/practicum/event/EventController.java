package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.random.EasyRandom;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.FullEventResponseDto;
import ru.practicum.event.dto.GetEventDto;
import ru.practicum.requests.dto.EventRequestsPatchDto;
import ru.practicum.requests.dto.FullRequestsDto;
import ru.practicum.requests.dto.ex.StatusListFullRequestDto;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    private final EasyRandom easyRandom = new EasyRandom();

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FullEventResponseDto postEvent(@RequestBody @Valid EventDto eventDto, @PathVariable Long userId) {
        log.debug("POST /users/{}/events with dto: {}.", userId, eventDto);
        return eventService.postEvent(eventDto, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<GetEventDto> getEvents(@PathVariable Long userId) {
        log.debug("GET /users/{}/events.", userId);
        return eventService.getEvents(userId);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public FullEventResponseDto getEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        log.debug("GET /users/{}/events.", userId);
        return eventService.getEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public FullEventResponseDto patchEvent(@PathVariable Long userId,
                                           @PathVariable Long eventId,
                                           @RequestBody @Valid EventDto eventDto) {
        log.debug("GET /users/{}/events.", userId);
        return eventService.patchEvent(userId, eventId, eventDto);
    }

    @GetMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<FullRequestsDto> getRequests(@PathVariable Long userId,
                                             @PathVariable Long eventId) {
        log.debug("GET /users/{}/events.", userId);
        return eventService.getRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public StatusListFullRequestDto patchRequests(@PathVariable Long userId,
                                                  @PathVariable Long eventId,
                                                  @RequestBody @Valid EventRequestsPatchDto dto) {
        log.debug("GET /users/{}/events.", userId);
        return eventService.patchRequests(userId, eventId, dto);
    }

}
