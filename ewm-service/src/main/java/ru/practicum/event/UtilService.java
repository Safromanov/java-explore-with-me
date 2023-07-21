package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.requests.EventRequestRepository;

@Service
@RequiredArgsConstructor
public class UtilService {

    private final EventRequestRepository eventRequestRepository;

    public int findViews() {
        return 0;
    }

    public int findCountRequests(long eventId) {
        return eventRequestRepository.countByStatusConfirmed(eventId);
    }
}
