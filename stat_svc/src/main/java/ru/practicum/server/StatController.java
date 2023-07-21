package ru.practicum.server;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.dto.GetStatDto;
import ru.practicum.model.dto.HitDto;
import ru.practicum.server.service.StatisticsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class StatController {

    private final StatisticsService statisticsService;

    @PostMapping("/hit")
    public ResponseEntity<Object> addStatisticalData(@RequestBody @Valid HitDto hitDto) {
        log.debug("POST /hit with dto: {}.", hitDto);
        statisticsService.addStatisticalData(hitDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<GetStatDto> getStatistics(@RequestParam LocalDateTime start,
                                          @RequestParam LocalDateTime end,
                                          @RequestParam(required = false) String[] uris,
                                          @RequestParam(defaultValue = "false") boolean unique) {
        log.debug("GET /stats with params: {}, {}, {}, {}", start, end, uris, unique);
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start must be before end");
        }
        return statisticsService.getStatistics(start, end, uris, unique);
    }
}


