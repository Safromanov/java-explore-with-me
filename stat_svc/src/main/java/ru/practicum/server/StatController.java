package ru.practicum.server;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.dto.GetDto;
import ru.practicum.model.dto.HitDto;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class StatController {

    private final StatisticsService statisticsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity addStatisticalData(@RequestBody HitDto dtoReq) {
        statisticsService.addStatisticalData(dtoReq);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<GetDto> getStatistics(@RequestParam LocalDateTime start,
                                      @RequestParam LocalDateTime end,
                                      @RequestParam String[] uris,
                                      @RequestParam boolean unique) {
        return statisticsService.getStatistics(start, end, uris, unique);
    }
}


