package ru.practicum.compilations.publics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilations.dto.CompilationCreateResponse;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.compilations.model.CompilationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PublicCompilationsService {
    private final CompilationRepository compilationRepository;
    private final ModelMapper modelMapper;


    public List<CompilationCreateResponse> getCompilationsByParam(Boolean pinned, int from, int size) {
        List<Compilation> compilationList = compilationRepository.findByParams(pinned, getPageRequest(from, size));
        log.info("Result: {}", compilationList.size());

        return compilationList.stream().map(compilation -> modelMapper.map(compilation, CompilationCreateResponse.class))
                .collect(Collectors.toList());
    }

    private PageRequest getPageRequest(int from, int size) {
        return PageRequest.of(from > 0 ? from / size : 0, size);
    }

    public CompilationCreateResponse getCompilation(long compIp) {
        var compilation = compilationRepository.findById(compIp)
                .orElseThrow(() -> new RuntimeException("Compilation not found"));
        return modelMapper.map(compilation, CompilationCreateResponse.class);
    }
}
