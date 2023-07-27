package ru.practicum.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.dto.GetStatDto;
import ru.practicum.dto.HitDto;
import ru.practicum.model.StatData;
import ru.practicum.model.StatDataMapper;
import ru.practicum.server.StatDataRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final StatDataRepository statDataRepository;

    @Override
    public void addStatisticalData(HitDto dtoReq) {
        StatData statData = StatDataMapper.fromDto(dtoReq);
        statDataRepository.save(statData);
    }

    @Override
    public List<GetStatDto> getStatistics(LocalDateTime start,
                                          LocalDateTime end,
                                          String[] uris,
                                          boolean unique) {
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
