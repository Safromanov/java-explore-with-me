package ru.practicum.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        return mapper;
    }
}

