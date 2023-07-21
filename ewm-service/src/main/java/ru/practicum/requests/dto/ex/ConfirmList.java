package ru.practicum.requests.dto.ex;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.requests.dto.FullRequestsDto;

import java.util.List;

@Data
@AllArgsConstructor
public class ConfirmList implements StatusListFullRequestDto {
    public List<FullRequestsDto> confirmedRequests;
}
