package ru.practicum.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.EventRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;
import ru.practicum.exceptionHandler.ConflictException;
import ru.practicum.exceptionHandler.NotFoundException;
import ru.practicum.requests.dto.EventRequestDto;
import ru.practicum.requests.model.EventRequest;
import ru.practicum.requests.model.Status;
import ru.practicum.user.model.User;
import ru.practicum.user.model.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EventRequestService {

    private final EventRequestRepository eventRequestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;


    public EventRequestDto postNewEventRequest(long userId, long eventId) {
        User requester = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User dont found"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event dont found"));
        if (event.getState() != State.PUBLISHED)
            throw new ConflictException("Event is not published");
        int confirmRequests = eventRequestRepository.countByStatusConfirmed(eventId);
        if (event.getParticipantLimit() <= confirmRequests && event.getParticipantLimit() != 0)
            throw new ConflictException("Event is full");
        if (requester.getId().equals(event.getInitiator().getId()))
            throw new ConflictException("You can't request to your event");
        eventRequestRepository.findByRequesterIdAndEventId(userId, eventId).ifPresent((x) -> {
            throw new ConflictException("Request already exist");
        });


        EventRequest eventRequest = new EventRequest();
        eventRequest.setCreated(LocalDateTime.now());
        eventRequest.setRequester(requester);
        eventRequest.setEvent(event);
        if (event.getParticipantLimit() != 0 && event.getRequestModeration())
            eventRequest.setStatus(Status.PENDING);
        else eventRequest.setStatus(Status.CONFIRMED);

        eventRequest = eventRequestRepository.save(eventRequest);
        log.info("Создан запрос со статусом - {}", eventRequest.getStatus());
        return modelMapper.map(eventRequest, EventRequestDto.class);
    }

    public List<EventRequestDto> getEventRequests(long requesterId) {
        userRepository.findById(requesterId).orElseThrow(() -> new NotFoundException("User dont found"));
        List<EventRequest> eventRequests = eventRequestRepository.findByRequesterId(requesterId);
        return eventRequests.stream()
                .map(eventRequest -> modelMapper.map(eventRequest, EventRequestDto.class))
                .collect(Collectors.toList());
    }

    public EventRequestDto cancelEventRequests(long requesterId, long eventRequestId) {
        userRepository.findById(requesterId).orElseThrow(() -> new NotFoundException("User dont found"));

        EventRequest eventRequest = eventRequestRepository
                .findByRequesterIdAndId(requesterId, eventRequestId)
                .orElseThrow(() -> new NotFoundException("User dont found"));
        eventRequest.setStatus(Status.CANCELED);
        return modelMapper.map(eventRequestRepository.save(eventRequest), EventRequestDto.class);
    }
}
