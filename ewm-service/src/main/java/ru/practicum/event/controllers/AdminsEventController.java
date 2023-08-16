package ru.practicum.event.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.FullEventResponseDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.model.SortEvent;
import ru.practicum.event.service.EventServiceImpl;
import ru.practicum.exceptionHandler.BadRequestException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class AdminsEventController {

    private final EventServiceImpl eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<FullEventResponseDto> getEventsByParam(@RequestParam(required = false) String text,
                                                       @RequestParam(required = false) Set<Long> categories,
                                                       @RequestParam(required = false) Boolean paid,
                                                       @RequestParam(required = false) LocalDateTime rangeStart,
                                                       @RequestParam(required = false) LocalDateTime rangeEnd,
                                                       @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                       @RequestParam(required = false) SortEvent sort,
                                                       @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                       @RequestParam(defaultValue = "10") @Positive int size, HttpServletRequest request) {
        log.info("GET /admin/events with params: {}.",
                request.getQueryString());
        var res = eventService.getEventsByParamForAdmin(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        log.info("Get ADMIN END");
        return res;
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public FullEventResponseDto updateEvent(@PathVariable @Positive long eventId, @RequestBody @Valid UpdateEventAdminRequest dto) {
        log.info("PATCH /admin/events/{} with dto: {}.", eventId, dto);
        if (dto.getEventDate() != null) {
            if (dto.getEventDate().isBefore(LocalDateTime.now()))
                throw new BadRequestException("Field: eventDate. The event must start no later than current time.");
        }
        return eventService.updateEvent(eventId, dto);
    }


}
