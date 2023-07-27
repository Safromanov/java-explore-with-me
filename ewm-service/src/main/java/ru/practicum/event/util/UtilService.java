package ru.practicum.event.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.StatisticsClient;
import ru.practicum.dto.ClientStatDto;
import ru.practicum.dto.GetStatDto;
import ru.practicum.requests.EventRequestRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UtilService {
    private final StatisticsClient staticsClient;

    private final EventRequestRepository eventRequestRepository;

    //возвращает мапу с id события в ключе и количеством просмотров в значении
    public Map<Long, Long> findViews(ClientStatDto s) throws JsonProcessingException {
        List<GetStatDto> dtoStatList = staticsClient.getStaticsForUri(s);
        Pattern pattern = Pattern.compile("/\\d/(\\d)/");
        Map<Long, Long> map = new HashMap<>();
        for (GetStatDto dto : dtoStatList) {
            Matcher matcher = pattern.matcher(dto.getUri());
            Long eventId = matcher.find() ? Long.parseLong(matcher.group(1)) : 0;
            map.put(eventId, dto.getHits());
        }
        return map;
    }

    public void addHit(HttpServletRequest request) {
        staticsClient.addHit("ewm-main-service", request);
    }

    public int findCountRequests(long eventId) {
        return eventRequestRepository.countByStatusConfirmed(eventId);
    }
}
