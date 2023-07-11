package ru.practicum;

public class StatDataMapper {

    public static StatData fromDto(PostDtoStatReq dto) {
        StatData statData = new StatData();
        statData.setApp(dto.getApp());
        statData.setUri(dto.getUri());
        statData.setIp(dto.getIp());
        statData.setTimestamp(dto.getTimestamp());
        return  statData;
    }
}
