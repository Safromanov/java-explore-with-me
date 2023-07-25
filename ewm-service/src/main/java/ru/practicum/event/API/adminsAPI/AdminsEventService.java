package ru.practicum.event.API.adminsAPI;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.event.EventRepository;
import ru.practicum.event.dto.FullEventResponseDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.SortEvent;
import ru.practicum.event.model.State;
import ru.practicum.exceptionHandler.ConflictException;
import ru.practicum.exceptionHandler.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminsEventService {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;

    public List<FullEventResponseDto> getEventsByParam(String text, Set<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, SortEvent sort, int from, int size) {
        PageRequest pageRequest = getPageRequest(from, size);
        if (sort == SortEvent.EVENT_DATE) pageRequest.withSort(Sort.by("eventDate"));

        List<Event> eventsByParam;
        if (onlyAvailable) {
            eventsByParam = eventRepository.getAvailableEventsByParam(text, categories, paid, rangeStart, rangeEnd, pageRequest);
        } else {
            eventsByParam = eventRepository.getEventsByParam(text, categories, paid, rangeStart, rangeEnd, pageRequest);
        }

        return eventsByParam.stream().map(event -> {
            var responseDto = modelMapper.map(event, FullEventResponseDto.class);
            if (event.getState() == State.CANCELED) responseDto.setParticipantLimit(0);
            return responseDto;
        }).collect(Collectors.toList());
    }

    private PageRequest getPageRequest(int from, int size) {
        return PageRequest.of(from > 0 ? from / size : 0, size);
    }

    public FullEventResponseDto patchEvent(long eventId, UpdateEventAdminRequest dto) {
        if (dto.getParticipantLimit() != null)
            if (dto.getParticipantLimit() == 0) dto.setParticipantLimit(null);
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event dont found"));
        modelMapper.map(dto, event);
        if (dto.getStateAction() != null) {
            switch (dto.getStateAction()) {
                case PUBLISH_EVENT:
                    if (event.getState().equals(State.PENDING))
                        event.setState(State.PUBLISHED);
                    else
                        throw new ConflictException("Cannot publish the event because it's not in the right state: " + event.getState());
                    break;
                case REJECT_EVENT:
                    if (!event.getState().equals(State.PUBLISHED)) {
                        event.setState(State.CANCELED);
                    } else
                        throw new ConflictException("Cannot reject the event because it's not in the right state: " + event.getState());
            }
        }
        event = eventRepository.save(event);
        var responseDto = modelMapper.map(event, FullEventResponseDto.class);
        if (event.getState() == State.CANCELED) responseDto.setParticipantLimit(0);
        return responseDto;
    }


}
