package ru.practicum.event.controllers.publicAPI;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.event.EventRepository;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.FullEventResponseDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.SortEvent;
import ru.practicum.event.model.State;
import ru.practicum.event.util.UtilService;
import ru.practicum.exceptionHandler.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;

    private final UtilService utilService;

    public List<EventShortDto> getEventsByParam(String text, Set<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                                LocalDateTime rangeEnd, boolean onlyAvailable, SortEvent sort,
                                                int from, int size, HttpServletRequest request) {
        PageRequest pageRequest = getPageRequest(from, size);
        if (sort == SortEvent.EVENT_DATE) pageRequest.withSort(Sort.by("eventDate"));

        Page<Event> eventsByParam;
        if (onlyAvailable) {
            eventsByParam = eventRepository.getAvailableEventsByParam(text, categories, paid, rangeStart, rangeEnd, pageRequest);
        } else {
            eventsByParam = eventRepository.getEventsByParam(text, categories, paid, rangeStart, rangeEnd, pageRequest);
        }
        utilService.addHit(request);
        return eventsByParam.get().map(event -> modelMapper.map(event, EventShortDto.class)).collect(Collectors.toList());
    }


    public FullEventResponseDto getEvent(long eventId, HttpServletRequest request) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event dont found"));
        if (event.getState() != State.PUBLISHED) throw new NotFoundException("Event dont found");
        FullEventResponseDto dto = modelMapper.map(event, FullEventResponseDto.class);
        Map<Long, Long> views = utilService.findViews(List.of(event));
        log.info(views.toString());
        utilService.addHit(request);
        dto.setViews(!views.containsKey(eventId) ? 0 : views.get(eventId));
        return dto;
    }

    private PageRequest getPageRequest(int from, int size) {
        return PageRequest.of(from > 0 ? from / size : 0, size);
    }
}