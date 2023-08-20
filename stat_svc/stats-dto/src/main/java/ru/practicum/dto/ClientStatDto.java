package ru.practicum.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
@Getter
@ToString
public class ClientStatDto {

    private LocalDateTime start;
    private LocalDateTime end;
    private List<String> uris;
    private boolean unique;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        if (start != null)
            map.put("start", start);
        if (end != null)
            map.put("end", end);
        if (uris != null)
            map.put("uris", String.join(",", uris));
        map.put("unique", unique);
        return map;
    }
}
