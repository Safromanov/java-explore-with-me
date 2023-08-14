package ru.practicum.compilations.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilations.dto.CompilationCreateResponse;
import ru.practicum.compilations.dto.CompilationDTOPostAdmin;
import ru.practicum.compilations.dto.Create;
import ru.practicum.event.dto.FullEventResponseDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Validated
public class AdminCompilationsController {

    private final AdminCompilationsService adminCompilationsService;

    @PostMapping
    @Validated(Create.class)
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationCreateResponse createCompilation(@RequestBody @Valid CompilationDTOPostAdmin compilationDDto, HttpServletRequest request) {
        log.info("Create Compilation {} with dto: {}.", request.getRequestURI(), compilationDDto);
        return adminCompilationsService.createCompilation(compilationDDto, request);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public CompilationCreateResponse updateCompilation(@RequestBody @Valid CompilationDTOPostAdmin compilationDDto,
                                                       @RequestParam Long compId,  HttpServletRequest request) {
        log.info("Update Compilation {} with dto: {}.", compId, compilationDDto);
        return adminCompilationsService.updateCompilation(compilationDDto, compId);
    }
}
