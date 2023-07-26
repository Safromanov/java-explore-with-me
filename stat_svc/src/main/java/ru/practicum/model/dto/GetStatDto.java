package ru.practicum.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class GetStatDto {

    private final String app;
    private final String uri;
    private final Long hits;
}
