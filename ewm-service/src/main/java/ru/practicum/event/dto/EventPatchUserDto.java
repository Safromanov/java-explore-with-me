package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.event.model.Location;
import ru.practicum.event.model.StateUserAction;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
@Getter
public class EventPatchUserDto {
    @Size(min = 20, max = 2000)
    private String annotation;

    private long category;
    @Size(min = 20, max = 15000)
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;
    @Size(min = 3, max = 120)
    private String title;

    private StateUserAction stateAction;
}

