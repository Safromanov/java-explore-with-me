package ru.practicum.compilations.service;

import ru.practicum.compilations.dto.CompilationCreateResponse;
import ru.practicum.compilations.dto.CompilationDtoReq;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CompilationsService {
    List<CompilationCreateResponse> getCompilationsByParam(Boolean pinned, int from, int size);

    CompilationCreateResponse getCompilation(long compIp);

    CompilationCreateResponse createCompilation(CompilationDtoReq compilationDDto, HttpServletRequest request);

    CompilationCreateResponse updateCompilation(CompilationDtoReq compilationDDto, Long compId);

    void deleteCompilation(Long compId);
}
