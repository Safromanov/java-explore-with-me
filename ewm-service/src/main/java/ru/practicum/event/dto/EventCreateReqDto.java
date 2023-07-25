package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.event.model.Location;
import ru.practicum.event.model.StateUserAction;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
@Getter
@JsonView(View.Post.class)
public class EventCreateReqDto {
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;
    @NotNull
    private long category;
    @NotBlank
    @Size(min = 20, max = 15000)
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    private LocalDateTime eventDate;
    @NotNull
    private Location location;
    @NotNull
    private Boolean paid;

    private int participantLimit;
    @NotNull
    private Boolean requestModeration;
    @NotBlank
    @Size(min = 3, max = 120)
    private String title;
    @JsonView(View.Patch.class)
    private StateUserAction stateAction;
}
