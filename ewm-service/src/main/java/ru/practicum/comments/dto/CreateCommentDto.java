package ru.practicum.comments.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateCommentDto {

    @Positive
    private Long event;

    @NotBlank
    private String text;
}
