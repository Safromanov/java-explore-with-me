package ru.practicum.requests.dto;

import lombok.Value;
import ru.practicum.requests.Status;

@Value
public class EventRequestsPatchDto {
    long[] requestIds;

    Status status;
}
