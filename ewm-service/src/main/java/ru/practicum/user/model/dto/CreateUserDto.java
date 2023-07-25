package ru.practicum.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class CreateUserDto {

    @NotBlank
    @Size(min = 6, max = 50)
    private String name;
    @Email
    @NotBlank
    private String email;
}
