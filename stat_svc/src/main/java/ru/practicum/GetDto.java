package ru.practicum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

//@AllArgsConstructor
@Getter
@ToString
public class GetDto {

    private final String app;

    private final String uri;
    private final Long hits;

    public GetDto(String app, String uri, Long hits) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }
}
