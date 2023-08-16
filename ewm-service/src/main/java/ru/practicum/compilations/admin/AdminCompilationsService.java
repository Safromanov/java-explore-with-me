package ru.practicum.compilations.admin;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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

@Service
@RequiredArgsConstructor
@Transactional
public class AdminCompilationsService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;

    public CompilationCreateResponse createCompilation(CompilationDtoReq compilationDDto, HttpServletRequest request) {
        List<Event> event = eventRepository.findByIdIn(compilationDDto.getEventIds());
        var compilation = CompilationMapper.createDTOToEntity(compilationDDto, event);
        compilation = compilationRepository.save(compilation);
        return modelMapper.map(compilation, CompilationCreateResponse.class);
    }

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

    public void deleteCompilation(Long compId) {
        compilationRepository.findById(compId).ifPresentOrElse(compilationRepository::delete, () -> {
            throw new NotFoundException("User dont found");
        });
    }
}
