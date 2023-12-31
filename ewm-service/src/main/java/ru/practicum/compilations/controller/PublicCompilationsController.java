package ru.practicum.compilations.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilations.dto.CompilationCreateResponse;
import ru.practicum.compilations.service.CompilationsService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class PublicCompilationsController {
    private final CompilationsService compilationsService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationCreateResponse> getCompilationsByParam(@RequestParam(required = false) Boolean pinned,
                                                                  @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                                  @RequestParam(defaultValue = "10") @Positive int size,
                                                                  HttpServletRequest request) {
        log.info("GET Compilations with params: {}, {}, {}.",
                request.getQueryString(), from, size);

        return compilationsService.getCompilationsByParam(pinned, from, size);
    }

    @GetMapping("{compIp}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationCreateResponse getCompilation(@PathVariable @Positive long compIp) {
        log.info("GET Compilation : {}", compIp);
        return compilationsService.getCompilation(compIp);
    }
}
