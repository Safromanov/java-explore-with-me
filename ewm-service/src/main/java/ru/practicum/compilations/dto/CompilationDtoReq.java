package ru.practicum.compilations.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@NoArgsConstructor
@Setter
@Getter
@ToString
public class CompilationDtoReq {

    @JsonProperty("events")
    public Set<Long> eventIds;

    public Boolean pinned;
    @NotBlank(groups = Create.class)
    @Size(min = 6, max = 50)
    public String title;
}
