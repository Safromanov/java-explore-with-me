package ru.practicum.event.API.privateAPI;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventCreateReqDto;
import ru.practicum.event.dto.EventPatchUserDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.FullEventResponseDto;
import ru.practicum.exceptionHandler.BadRequestException;
import ru.practicum.requests.dto.EventRequestsPatchDto;
import ru.practicum.requests.dto.FullRequestsDto;
import ru.practicum.requests.dto.StatusListRequestDto;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Validated
public class PrivateEventController {
    private final PrivateEventService privateEventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FullEventResponseDto postEvent(@RequestBody @Valid EventCreateReqDto eventDto, @PathVariable Long userId) {
        log.debug("POST /users/{}/events with dto: {}.", userId, eventDto);
        if (eventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2)))
            throw new BadRequestException("Field: eventDate. The event must start no later than 2 hours from the current time.");
        return privateEventService.postEvent(eventDto, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEvents(@PathVariable Long userId) {
        log.debug("GET /users/{}/events.", userId);
        return privateEventService.getEvents(userId);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public FullEventResponseDto getEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        log.debug("GET /users/{}/events.", userId);
        return privateEventService.getEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public FullEventResponseDto patchEvent(@PathVariable Long userId,
                                           @PathVariable Long eventId,
                                           @RequestBody @Valid EventPatchUserDto eventDto) {
        log.debug("GET /users/{}/events.", userId);
        if (eventDto.getEventDate() != null)
            if (eventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2)))
                throw new BadRequestException("Field: eventDate. The event must start no later than 2 hours from the current time.");
        return privateEventService.patchEvent(userId, eventId, eventDto);
    }

    @GetMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<FullRequestsDto> getRequests(@PathVariable Long userId,
                                             @PathVariable Long eventId) {
        log.debug("GET /users/{}/events.", userId);
        return privateEventService.getRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public StatusListRequestDto patchRequests(@PathVariable Long userId,
                                              @PathVariable Long eventId,
                                              @RequestBody @Valid EventRequestsPatchDto dto) {
        log.debug("GET /users/{}/events.", userId);
        return privateEventService.patchRequests(userId, eventId, dto);
    }

}
