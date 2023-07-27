package ru.practicum.server.service;


import ru.practicum.dto.GetStatDto;
import ru.practicum.dto.HitDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticsService {
    void addStatisticalData(HitDto dtoReq);

    List<GetStatDto> getStatistics(LocalDateTime start,
                                   LocalDateTime end,
                                   String[] uris,
                                   boolean unique);
}
