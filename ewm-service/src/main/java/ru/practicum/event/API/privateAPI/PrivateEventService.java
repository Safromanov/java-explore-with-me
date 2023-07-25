package ru.practicum.event.API.privateAPI;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.Category;
import ru.practicum.category.CategoryRepository;
import ru.practicum.event.EventRepository;
import ru.practicum.event.UtilService;
import ru.practicum.event.dto.EventCreateReqDto;
import ru.practicum.event.dto.EventPatchUserDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.FullEventResponseDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventMapper;
import ru.practicum.event.model.State;
import ru.practicum.exceptionHandler.ConflictException;
import ru.practicum.exceptionHandler.NotFoundException;
import ru.practicum.requests.EventRequestRepository;
import ru.practicum.requests.dto.EventRequestsPatchDto;
import ru.practicum.requests.dto.FullRequestsDto;
import ru.practicum.requests.dto.StatusListRequestDto;
import ru.practicum.requests.model.EventRequest;
import ru.practicum.requests.model.Status;
import ru.practicum.user.model.User;
import ru.practicum.user.model.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PrivateEventService {

    private final EventRepository eventRepository;

    private final CategoryRepository categoryRepository;

    private final UserRepository userRepository;

    private final EventRequestRepository eventRequestRepository;

    private final UtilService utilService;

    private final ModelMapper modelMapper;

    public FullEventResponseDto postEvent(EventCreateReqDto eventDto, Long userId) {
        User initiator = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User dont found"));

        Category category = categoryRepository.findById(eventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Category dont found"));

        Event event = EventMapper.createDtoToEvent(eventDto, initiator, category);
        event = eventRepository.save(event);
        return EventMapper.toFullEventResponseDto(event, 0, 0);
    }

    public List<EventShortDto> getEvents(long userId) {
        User initiator = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User dont found"));
        return eventRepository.findAll().stream().map((x) ->
                EventMapper.toGetEventDto(x, initiator, 0, 0)).collect(Collectors.toList());
    }

    public FullEventResponseDto getEvent(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User dont found"));

        Event event = eventRepository.findByInitiatorIdAndId(userId, eventId)
                .orElseThrow(() -> new NotFoundException("Not found event"));
        var responseDto = EventMapper.toFullEventResponseDto(event, 0, 0);
        if (event.getState() == State.CANCELED) responseDto.setParticipantLimit(0);
        return responseDto;
    }


    public FullEventResponseDto patchEvent(Long userId, Long eventId, EventPatchUserDto eventDto) {
        if (eventDto.getParticipantLimit() != null)
            if (eventDto.getParticipantLimit() == 0)
                eventDto.setParticipantLimit(null);
        User initiator = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User dont found"));
        Event event = eventRepository.findByInitiatorIdAndId(userId, eventId)
                .orElseThrow(() -> new NotFoundException("Not found event"));
        if (event.getState() == State.PUBLISHED)
            throw new ConflictException("Only pending or canceled events can be changed");
        modelMapper.map(eventDto, event);
        event = eventRepository.save(event);
        switch (eventDto.getStateAction()) {
            case CANCEL_REVIEW:
                event.setState(State.CANCELED);
                break;
            case SEND_TO_REVIEW:
                event.setState(State.PENDING);
                break;
        }
        var responseDto = EventMapper.toFullEventResponseDto(event, 0, 0);
        if (event.getState() == State.CANCELED) responseDto.setParticipantLimit(0);
        return responseDto;
    }

    public List<FullRequestsDto> getRequests(Long userId, Long eventId) {
        return eventRequestRepository.findAllByEvent_InitiatorIdAndEventId(userId, eventId);
    }

    public StatusListRequestDto patchRequests(Long userId, Long eventId, EventRequestsPatchDto dto) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User dont found"));

        Event event = eventRepository.findByInitiatorIdAndId(userId, eventId)
                .orElseThrow(() -> new NotFoundException("Event dont found"));

        if (!event.getRequestModeration())
            return new StatusListRequestDto(eventRequestRepository.findConfirmDtoByIdIn(eventId, dto.getRequestIds()),
                    eventRequestRepository.findRejectedDtoByIdIn(eventId, dto.getRequestIds()));

        int countConfirmed = eventRequestRepository.countByStatusConfirmed(eventId);

        List<EventRequest> eventRequests = eventRequestRepository.findByIdIn(userId, dto.getRequestIds());

        if (countConfirmed >= event.getParticipantLimit())
            throw new ConflictException("The participant limit has been reached");

        for (var eventRequest : eventRequests) {
            if (eventRequest.getStatus() != Status.PENDING)
                throw new ConflictException("Status can be changed only for requests that are in the pending state");
            if (countConfirmed < event.getParticipantLimit()) {
                eventRequest.setStatus(dto.getStatus());
                countConfirmed++;
            } else eventRequest.setStatus(Status.REJECTED);
            eventRequestRepository.save(eventRequest);
        }
        return new StatusListRequestDto(eventRequestRepository.findConfirmDtoByIdIn(eventId, dto.getRequestIds()),
                eventRequestRepository.findRejectedDtoByIdIn(eventId, dto.getRequestIds()));
    }
}
