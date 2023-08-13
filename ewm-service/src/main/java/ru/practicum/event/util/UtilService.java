package ru.practicum.event.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.StatisticsClient;
import ru.practicum.dto.ClientStatDto;
import ru.practicum.dto.GetStatDto;
import ru.practicum.event.model.Event;
import ru.practicum.requests.EventRequestRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UtilService {
    private final StatisticsClient staticsClient;

    private final EventRequestRepository eventRequestRepository;

    public Map<Long, Long> findViews(List<Event> events) {

        ClientStatDto clientStatDto =  ClientStatDto.builder().unique(true).uris(events.stream().map(Event::getUri).collect(Collectors.toList())).build();
        log.info("clientStatDto -" + clientStatDto);
        List<GetStatDto> dtoStatList;
        try {
            dtoStatList = staticsClient.getStaticsForUri(clientStatDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Map<Long, Long> map = new HashMap<>();
        for (var dto : dtoStatList) {
            String[] parts = dto.getUri().split("/");
            Long eventId = Long.parseLong(parts[2]);
            map.put(eventId, dto.getHits());
        }
        return map;
    }

    public void addHit(HttpServletRequest request) {
        staticsClient.addHit("ewm-main-service", request.getRequestURI(), request);
    }

    public int findCountRequests(long eventId) {
        return eventRequestRepository.countByStatusConfirmed(eventId);
    }
}
