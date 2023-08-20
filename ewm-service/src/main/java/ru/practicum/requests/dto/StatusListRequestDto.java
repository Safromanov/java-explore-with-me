package ru.practicum.requests.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StatusListRequestDto {

    public List<FullRequestsDto> confirmedRequests;

    public List<FullRequestsDto> rejectedRequests;
}
