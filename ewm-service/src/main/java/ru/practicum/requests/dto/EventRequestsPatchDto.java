package ru.practicum.requests.dto;

import lombok.Value;
import ru.practicum.requests.model.Status;

import java.util.Set;

@Value
public class EventRequestsPatchDto {
    Set<Long> requestIds;

    Status status;
}
