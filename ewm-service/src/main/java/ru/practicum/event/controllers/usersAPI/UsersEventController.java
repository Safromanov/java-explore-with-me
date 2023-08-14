package ru.practicum.event.controllers.usersAPI;

import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StreamUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventCreateDto;
import ru.practicum.event.dto.EventPatchUserDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.FullEventResponseDto;
import ru.practicum.exceptionHandler.BadRequestException;
import ru.practicum.requests.dto.EventRequestsPatchDto;
import ru.practicum.requests.dto.FullRequestsDto;
import ru.practicum.requests.dto.StatusListRequestDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Validated
public class UsersEventController {
    private final UsersEventService usersEventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FullEventResponseDto postEvent(@RequestBody @Valid EventCreateDto eventDto, @PathVariable Long userId, HttpServletRequest request) {
        log.info("POST {} with dto: {}.", request.getRequestURI(), eventDto);
        if (eventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2)))
            throw new BadRequestException("Field: eventDate. The event must start no later than 2 hours from the current time.");
        return usersEventService.postEvent(eventDto, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEvents(@PathVariable Long userId,
                                         @RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "10") @Min(1) int size) {
        log.info("GET /users/{}/events.", userId);
        return usersEventService.getEvents(userId, from, size);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public FullEventResponseDto getEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("GET /users/{}/events.", userId);
        return usersEventService.getEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public FullEventResponseDto patchEvent(@PathVariable Long userId,
                                           @PathVariable Long eventId,
                                           @RequestBody @Valid EventPatchUserDto eventDto) {
        log.info("GET /users/{}/events.", userId);
        if (eventDto.getEventDate() != null)
            if (eventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2)))
                throw new BadRequestException("Field: eventDate. The event must start no later than 2 hours from the current time.");
        return usersEventService.patchEvent(userId, eventId, eventDto);
    }

    @GetMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<FullRequestsDto> getRequests(@PathVariable Long userId,
                                             @PathVariable Long eventId) {
        log.info("GET /users/{}/events.", userId);
        return usersEventService.getRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public StatusListRequestDto patchRequests(@PathVariable Long userId,
                                              @PathVariable Long eventId,
                                              @RequestBody @Valid EventRequestsPatchDto dto, HttpServletRequest request) {
        log.info("Patch Request {} with dto: {}.", request.getRequestURI(), dto);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return usersEventService.patchRequests(userId, eventId, dto);
    }

}
