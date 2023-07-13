package ru.practicum.server;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.model.StatDataMapper;
import ru.practicum.model.dto.GetStatDto;
import ru.practicum.model.dto.HitDto;
import ru.practicum.model.StatData;

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
        if (unique)
            return statDataRepository.getStatisticForUrisWithUniqueIp(start, end, uris);
        return statDataRepository.getStatisticForUris(start, end, uris);
    }
}
