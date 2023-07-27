package ru.practicum.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Builder
@Getter
public class ClientStatDto {

    private LocalDateTime start;
    private LocalDateTime end;
    private String[] uris;
    private boolean unique;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("start", start);
        map.put("end", end);
        map.put("uris", uris);
        map.put("unique", unique);
        return map;
    }
}
