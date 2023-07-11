package ru.practicum.server;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.model.StatDataMapper;
import ru.practicum.model.dto.GetDto;
import ru.practicum.model.dto.PostDtoStatReq;
import ru.practicum.model.StatData;
import ru.practicum.server.StatDataRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final StatDataRepository statDataRepository;

    public void addStatisticalData(PostDtoStatReq dtoReq) {
        StatData statData = StatDataMapper.fromDto(dtoReq);
        statDataRepository.save(statData);
    }

    public List<GetDto> getStatistics(@RequestParam LocalDateTime start,
                                      @RequestParam LocalDateTime end,
                                      @RequestParam String[] uris,
                                      @RequestParam boolean unique) {
        if (unique)
            return statDataRepository.getStatisticForUrisWithUniqueIp(start, end, uris);
        return statDataRepository.getStatisticForUris(start, end, uris);
    }
}
