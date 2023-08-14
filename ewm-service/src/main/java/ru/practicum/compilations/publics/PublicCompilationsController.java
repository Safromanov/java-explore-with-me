package ru.practicum.compilations.publics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilations.dto.CompilationCreateResponse;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class PublicCompilationsController {
    private final PublicCompilationsService compilationsService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationCreateResponse> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                           @RequestParam(defaultValue = "0") int from,
                                                           @RequestParam(defaultValue = "10") @Min(1) int size,
                                                           HttpServletRequest request) {
        log.info("GET Compilations with params: {}, {}, {}.",
                request.getQueryString(), from, size);

        var result = compilationsService.getCompilations(pinned, from, size);
        log.info("Result: {}", result);
        return result;
    }

    @GetMapping("{compIp}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationCreateResponse getCompilation(@PathVariable long compIp,
                                                    HttpServletRequest request) {
        log.info("GET Compilation : {}", compIp);
        return compilationsService.getCompilation(compIp);
    }
}
