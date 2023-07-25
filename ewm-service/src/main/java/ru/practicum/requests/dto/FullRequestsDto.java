package ru.practicum.requests.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import ru.practicum.requests.model.Status;

import java.time.LocalDateTime;

public interface FullRequestsDto {

    @JsonProperty("event")
    @Value("#{target.event.id}")
    long getEventId();

    @JsonProperty("requester")
    @Value("#{target.requester.id}")
    long getRequesterId();

    Status getStatus();

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime getCreated();
}
