package ru.practicum.server.service;

import ru.practicum.model.dto.GetStatDto;
import ru.practicum.model.dto.HitDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticsServiceI {
    void addStatisticalData(HitDto dtoReq);

    List<GetStatDto> getStatistics(LocalDateTime start,
                                   LocalDateTime end,
                                   String[] uris,
                                   boolean unique);
}
