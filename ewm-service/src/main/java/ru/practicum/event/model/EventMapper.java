package ru.practicum.event.model;

import org.modelmapper.ModelMapper;
import ru.practicum.category.Category;
import ru.practicum.category.dto.ResponseCategoryDto;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.FullEventResponseDto;
import ru.practicum.event.dto.GetEventDto;
import ru.practicum.user.model.User;
import ru.practicum.user.model.dto.UserShortDto;

import java.time.LocalDateTime;


public class EventMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static Event createDtoToEvent(EventDto eventDto, User initiator, Category category) {
        Event event = new Event();

        event.setInitiator(initiator);
        event.setCategory(category);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(State.PENDING);

        event.setAnnotation(eventDto.getAnnotation());
        event.setEventDate(eventDto.getEventDate());
        event.setDescription(eventDto.getDescription());
        event.setPaid(eventDto.getPaid());
        event.setRequestModeration(eventDto.getRequestModeration());

        event.setTitle(eventDto.getTitle());
        event.setParticipantLimit(eventDto.getParticipantLimit());
        event.setLocation(eventDto.getLocation());
        return event;
    }

    public static FullEventResponseDto toFullEventResponseDto(Event event, long views, int confirmedRequests) {
        FullEventResponseDto fullEventResponseDto = new FullEventResponseDto();

        fullEventResponseDto.setCategory(modelMapper.map(event.getCategory(), ResponseCategoryDto.class));
        fullEventResponseDto.setId(event.getId());
        fullEventResponseDto.setDescription(event.getDescription());
        fullEventResponseDto.setAnnotation(event.getAnnotation());

        fullEventResponseDto.setCreatedOn(event.getCreatedOn());
        fullEventResponseDto.setEventDate(event.getEventDate());
        fullEventResponseDto.setPaid(event.getPaid());
        fullEventResponseDto.setLocation(event.getLocation());

        fullEventResponseDto.setInitiator(modelMapper.map(event.getInitiator(), UserShortDto.class));
        fullEventResponseDto.setState(event.getState());
        fullEventResponseDto.setViews(views);
        fullEventResponseDto.setConfirmedRequests(confirmedRequests);

        fullEventResponseDto.setTitle(event.getTitle());
        fullEventResponseDto.setParticipantLimit(event.getParticipantLimit());
        fullEventResponseDto.setRequestModeration(event.getRequestModeration());
        return fullEventResponseDto;
    }

    public static GetEventDto toGetEventDto(Event event, User initiator, int confirmedRequests, int views) {
        GetEventDto eventDto = new GetEventDto();
        eventDto.setId(event.getId());
        eventDto.setInitiator(modelMapper.map(initiator, UserShortDto.class));
        eventDto.setAnnotation(event.getAnnotation());
        eventDto.setCategory(modelMapper.map(event.getCategory(), ResponseCategoryDto.class));

        eventDto.setEventDate(event.getEventDate());
        eventDto.setPaid(event.getPaid());
        eventDto.setTitle(event.getTitle());
        eventDto.setConfirmedRequests(confirmedRequests);
        eventDto.setViews(views);
        return eventDto;
    }
}
