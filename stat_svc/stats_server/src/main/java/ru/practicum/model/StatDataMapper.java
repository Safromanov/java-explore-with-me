package ru.practicum.model;


import ru.practicum.dto.HitDto;

public class StatDataMapper {

    public static StatData fromDto(HitDto dto) {
        StatData statData = new StatData();
        statData.setApp(dto.getApp());
        statData.setUri(dto.getUri());
        statData.setIp(dto.getIp());
        statData.setTimestamp(dto.getTimestamp());
        return statData;
    }
}
