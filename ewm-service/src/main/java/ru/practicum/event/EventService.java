package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.practicum.category.Category;
import ru.practicum.category.CategoryRepository;
import ru.practicum.category.NotFoundException;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.FullEventResponseDto;
import ru.practicum.event.dto.GetEventDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventMapper;
import ru.practicum.requests.EventRequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.model.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    private final CategoryRepository categoryRepository;

    private final UserRepository userRepository;

    private final EventRequestRepository eventRequestRepository;

    private final UtilService utilService;

    private final ModelMapper modelMapper;

    public FullEventResponseDto postEvent(EventDto eventDto, Long userId) {
        User initiator = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User dont found"));

        Category category = categoryRepository.findById(eventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Category dont found"));

        Event event = EventMapper.createDtoToEvent(eventDto, initiator, category);
        event = eventRepository.save(event);
        return EventMapper.toFullEventResponseDto(event, 0, 0);
    }

    public List<GetEventDto> getEvents(long userId) {
        User initiator = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User dont found"));
        return eventRepository.findAll().stream().map((x) ->
                EventMapper.toGetEventDto(x, initiator, 0, 0)).collect(Collectors.toList());
    }

    public FullEventResponseDto getEvent(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User dont found"));
        //  Event event = eventRepository.findById(eventId).get();
        Event event = eventRepository.findByInitiatorIdAndId(userId, eventId)
                .orElseThrow(() -> new NotFoundException("Not found event"));
        return EventMapper.toFullEventResponseDto(event, 0, 0);
    }


    public FullEventResponseDto patchEvent(Long userId, Long eventId, EventDto eventDto) {
        User initiator = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User dont found"));
        Event event = eventRepository.findByInitiatorIdAndId(userId, eventId)
                .orElseThrow(() -> new NotFoundException("Not found event"));
        modelMapper.map(eventDto, event);
        event = eventRepository.save(event);
        return EventMapper.toFullEventResponseDto(event, 0, 0);
    }
}
