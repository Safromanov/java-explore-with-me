package ru.practicum.requests;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EventRequestService {

    public EventRequestDto postEventRequest(long userId, long eventId) {
        return new EventRequestDto(1L, LocalDateTime.now(), Status.PENDING);
    }
}
