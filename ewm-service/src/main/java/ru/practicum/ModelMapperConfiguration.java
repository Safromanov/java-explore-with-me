package ru.practicum;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.event.dto.FullEventResponseDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;
import ru.practicum.requests.dto.EventRequestDto;
import ru.practicum.requests.model.EventRequest;

@Configuration
public class ModelMapperConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setSkipNullEnabled(true);
        mapper.addMappings(new PropertyMap<EventRequest, EventRequestDto>() {
            @Override
            protected void configure() {
                map().setEvent(source.getEvent().getId());
                map().setRequester(source.getRequester().getId());
            }
        });


        mapper.addMappings(new PropertyMap<Event, FullEventResponseDto>() {
            @Override
            protected void configure() {
                using(context -> context.getSource() == State.PUBLISHED).map(source.getState(), destination.getPublishedOn());
            }
        });

        return mapper;
    }

}

