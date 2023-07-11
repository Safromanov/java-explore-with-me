package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

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
        return statDataRepository.getStatisticForUris(start, end, uris);
    }
}
