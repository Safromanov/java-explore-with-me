package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import ru.practicum.category.dto.ResponseCategoryDto;
import ru.practicum.comments.dto.CommentDtoResponse;
import ru.practicum.event.model.Location;
import ru.practicum.event.model.State;
import ru.practicum.user.model.dto.UserShortDto;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class FullEventResponseDto {

    private Long id;
    private UserShortDto initiator;
    @ToString.Exclude
    private String annotation;
    private ResponseCategoryDto category;
    @ToString.Exclude
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime publishedOn;
    private Location location;
    private Boolean paid;
    private int participantLimit;
    private Boolean requestModeration;
    private String title;
    private int confirmedRequests;
    private State state;
    private long views;
    private Set<CommentDtoResponse> comments;
}
