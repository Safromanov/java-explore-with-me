package ru.practicum.server;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.model.StatData;
import ru.practicum.model.StatDataMapper;
import ru.practicum.model.dto.GetStatDto;
import ru.practicum.model.dto.HitDto;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class StatisticsService {

    private final StatDataRepository statDataRepository;

    public void addStatisticalData(HitDto dtoReq) {
        StatData statData = StatDataMapper.fromDto(dtoReq);
        statDataRepository.save(statData);
    }

    public List<GetStatDto> getStatistics(@RequestParam LocalDateTime start,
                                          @RequestParam LocalDateTime end,
                                          @RequestParam String[] uris,
                                          @RequestParam boolean unique) {
        if (uris == null || uris.length == 0) {
            if (unique)
                return statDataRepository.getStatisticUniqueIp(start, end);
            return statDataRepository.getStatistic(start, end);
        }
        if (unique)
            return statDataRepository.getStatisticForUrisWithUniqueIp(start, end, uris);
        return statDataRepository.getStatisticForUris(start, end, uris);
    }
}
