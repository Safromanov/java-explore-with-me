package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.event.model.Location;
import ru.practicum.event.model.StateUserAction;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
@Getter
@JsonView(View.Create.class)
@ToString
public class EventCreateDto {
    @NotBlank
    @Size(min = 20, max = 2000)
    @ToString.Exclude
    private String annotation;
    @NotNull
    @ToString.Exclude
    private long category;
    @NotBlank
    @Size(min = 20, max = 15000)
    @ToString.Exclude
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    private LocalDateTime eventDate;
    @NotNull
    private Location location;
    @JsonProperty(defaultValue = "false")
    private Boolean paid;
    @JsonProperty(defaultValue = "0")
    private int participantLimit;
    @JsonProperty(defaultValue = "true")
    private Boolean requestModeration;
    @NotBlank
    @Size(min = 3, max = 120)
    private String title;
    @JsonView(View.Update.class)
    private StateUserAction stateAction;
}
