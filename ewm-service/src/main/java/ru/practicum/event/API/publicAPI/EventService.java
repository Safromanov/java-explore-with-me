package ru.practicum.event.API.publicAPI;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;

    private final UtilService utilService;

    public List<EventShortDto> getEventsByParam(String text, Set<Long> categories, Boolean paid,
                                                LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                boolean onlyAvailable, SortEvent sort, int from, int size,
                                                HttpServletRequest request) {
        PageRequest pageRequest = getPageRequest(from, size);
        if (sort == SortEvent.EVENT_DATE) pageRequest.withSort(Sort.by("eventDate"));

        List<Event> eventsByParam;
        if (onlyAvailable) {
            eventsByParam = eventRepository.getAvailableEventsByParam(text, categories, paid, rangeStart, rangeEnd, pageRequest);
        } else {
            eventsByParam = eventRepository.getEventsByParam(text, categories, paid, rangeStart, rangeEnd, pageRequest);
        }
        return eventsByParam.stream().map(event -> modelMapper.map(event, EventShortDto.class)).collect(Collectors.toList());
    }


    private PageRequest getPageRequest(int from, int size) {
        return PageRequest.of(from > 0 ? from / size : 0, size);
    }

    public FullEventResponseDto getEvent(long eventId, HttpServletRequest request) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event dont found"));
        if (event.getState() != State.PUBLISHED) throw new NotFoundException("Event dont found");
        FullEventResponseDto dto = modelMapper.map(event, FullEventResponseDto.class);
        //   dto.setViews(utilService.findViews(eventId));
        return dto;
    }
}