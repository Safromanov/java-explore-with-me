package ru.practicum.compilations.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilations.dto.CompilationCreateResponse;
import ru.practicum.compilations.dto.CompilationDtoReq;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.compilations.model.CompilationMapper;
import ru.practicum.compilations.model.CompilationRepository;
import ru.practicum.event.EventRepository;
import ru.practicum.event.model.Event;
import ru.practicum.exceptionHandler.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CompilationsServiceImpl implements CompilationsService {
    private final CompilationRepository compilationRepository;
    private final ModelMapper modelMapper;
    private final EventRepository eventRepository;


    @Override
    public List<CompilationCreateResponse> getCompilationsByParam(Boolean pinned, int from, int size) {
        List<Compilation> compilationList = compilationRepository.findByParams(pinned, getPageRequest(from, size));
        log.info("Result: {}", compilationList.size());

        return compilationList.stream().map(compilation -> modelMapper.map(compilation, CompilationCreateResponse.class))
                .collect(Collectors.toList());
    }

    private PageRequest getPageRequest(int from, int size) {
        return PageRequest.of(from > 0 ? from / size : 0, size);
    }

    @Override
    public CompilationCreateResponse getCompilation(long compIp) {
        var compilation = compilationRepository.findById(compIp)
                .orElseThrow(() -> new RuntimeException("Compilation not found"));
        return modelMapper.map(compilation, CompilationCreateResponse.class);
    }

    @Override
    public CompilationCreateResponse createCompilation(CompilationDtoReq compilationDDto, HttpServletRequest request) {
        List<Event> event = eventRepository.findByIdIn(compilationDDto.getEventIds());
        var compilation = CompilationMapper.createDTOToEntity(compilationDDto, event);
        compilation = compilationRepository.save(compilation);
        return modelMapper.map(compilation, CompilationCreateResponse.class);
    }

    @Override
    public CompilationCreateResponse updateCompilation(CompilationDtoReq compilationDDto, Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found"));
        if (compilationDDto.getEventIds() != null) {
            List<Event> events = eventRepository.findByIdIn(compilationDDto.getEventIds());
            compilation.setEvents(events);
        }
        if (compilationDDto.getPinned() != null) {
            compilation.setPinned(compilationDDto.getPinned());
        }
        if (compilationDDto.getTitle() != null) {
            compilation.setTitle(compilationDDto.getTitle());
        }
        compilation = compilationRepository.save(compilation);
        return modelMapper.map(compilation, CompilationCreateResponse.class);
    }

    @Override
    public void deleteCompilation(Long compId) {
        compilationRepository.findById(compId).ifPresentOrElse(compilationRepository::delete, () -> {
            throw new NotFoundException("User dont found");
        });
    }
}
