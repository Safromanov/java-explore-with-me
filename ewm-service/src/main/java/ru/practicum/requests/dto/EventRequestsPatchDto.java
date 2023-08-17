package ru.practicum.requests.dto;

import lombok.ToString;
import lombok.Value;
import ru.practicum.requests.model.Status;

import java.util.Set;

@Value
@ToString
public class EventRequestsPatchDto {

    Set<Long> requestIds;

    Status status;
}
