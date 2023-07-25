package ru.practicum.requests;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.practicum.event.EventRepository;
import ru.practicum.event.model.Event;
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
public class EventRequestService {

    private final EventRequestRepository eventRequestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;


    public EventRequestDto postEventRequest(long userId, long eventId) {
        User requester = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User dont found"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event dont found"));

        EventRequest eventRequest = new EventRequest();
        eventRequest.setCreated(LocalDateTime.now());
        eventRequest.setRequester(requester);
        eventRequest.setEvent(event);
        eventRequest.setStatus(Status.PENDING);

        eventRequest = eventRequestRepository.save(eventRequest);

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
        eventRequest.setStatus(Status.REJECTED);
        return modelMapper.map(eventRequestRepository.save(eventRequest), EventRequestDto.class);
    }
}
