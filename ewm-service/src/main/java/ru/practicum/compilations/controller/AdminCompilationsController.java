package ru.practicum.compilations.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilations.dto.CompilationCreateResponse;
import ru.practicum.compilations.dto.CompilationDtoReq;
import ru.practicum.compilations.dto.Create;
import ru.practicum.compilations.service.CompilationsServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Validated
public class AdminCompilationsController {

    private final CompilationsServiceImpl compilationsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationCreateResponse createCompilation(@RequestBody @Validated(Create.class) @Valid CompilationDtoReq compilationDDto,
                                                       HttpServletRequest request) {
        log.info("Create Compilation {} with dto: {}.", request.getRequestURI(), compilationDDto);
        return compilationsService.createCompilation(compilationDDto, request);
    }

    @PatchMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationCreateResponse updateCompilation(@RequestBody @Valid CompilationDtoReq compilationDDto,
                                                       @PathVariable @Positive Long compId) {
        log.info("Update Compilation {} with dto: {}.", compId, compilationDDto);
        return compilationsService.updateCompilation(compilationDDto, compId);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable @Positive Long compId) {
        log.info("Delete Compilation {}.", compId);
        compilationsService.deleteCompilation(compId);
    }
}
