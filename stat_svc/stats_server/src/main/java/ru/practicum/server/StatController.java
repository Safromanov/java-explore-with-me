package ru.practicum.server;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.GetStatDto;
import ru.practicum.dto.HitDto;
import ru.practicum.server.service.StatisticsServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class StatController {

    private final StatisticsServiceImpl statisticsService;

    @PostMapping(value = "/hit", consumes = "application/json")
    public ResponseEntity<Object> addStatisticalData(@RequestBody @Valid HitDto hitDto) {
        log.info("POST /hit with dto: {}.", hitDto);
        statisticsService.addStatisticalData(hitDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<GetStatDto> getStatistics(@RequestParam(defaultValue = "1972-05-05 00:00:00") LocalDateTime start, @RequestParam(defaultValue = "3020-05-05 00:00:00") LocalDateTime end, @RequestParam(required = false) List<String> uris, @RequestParam(defaultValue = "false") boolean unique, HttpServletRequest request) {
        log.info("Query " + request.getQueryString());

        if (start != null && end != null) if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start must be before end");
        }
        if ((start.getYear() == 1972 && end.getYear() != 3020) || (end.getYear() == 3020 && start.getYear() != 1972))
            throw new BadRequestException("Incorrect time range");
        return statisticsService.getStatistics(start, end, uris, unique);
    }
}


