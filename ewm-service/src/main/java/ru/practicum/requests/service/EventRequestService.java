package ru.practicum.requests.service;

import ru.practicum.requests.dto.EventRequestDto;

import java.util.List;

public interface EventRequestService {

    EventRequestDto createEventRequest(long userId, long eventId);

    List<EventRequestDto> getEventRequests(long requesterId);

    EventRequestDto cancelEventRequests(long requesterId, long eventRequestId);
}
