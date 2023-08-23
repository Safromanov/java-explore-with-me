package ru.practicum.comments.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateCommentDto {

    @Positive
    private Long id;

    @NotBlank
    @Size(min = 1, max = 20000)
    private String text;
}
