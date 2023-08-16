package ru.practicum.compilations.model;

import ru.practicum.compilations.dto.CompilationDtoReq;
import ru.practicum.event.model.Event;

import java.util.List;

public class CompilationMapper {
    public static Compilation createDTOToEntity(CompilationDtoReq dto, List<Event> events) {
        var compilation = new Compilation();
        compilation.setPinned(dto.getPinned() != null ? dto.getPinned() : false);
        compilation.setTitle(dto.getTitle());
        compilation.setEvents(events);
        return compilation;
    }
}
