package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.event.model.Action;
import ru.practicum.event.model.Location;

import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
@Getter
@JsonView(View.Post.class)
public class EventDto {

    private String annotation;
    private long category;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private int participantLimit;
    private Boolean requestModeration;
    private String title;
    @JsonView(View.Patch.class)
    private Action stateAction;
}
