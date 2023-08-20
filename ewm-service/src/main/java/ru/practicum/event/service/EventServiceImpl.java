package ru.practicum.event.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatisticsClient;
import ru.practicum.category.CategoryRepository;
import ru.practicum.category.model.Category;
import ru.practicum.comments.Comment;
import ru.practicum.comments.CommentMapper;
import ru.practicum.comments.CommentRepository;
import ru.practicum.comments.dto.CommentDtoResponse;
import ru.practicum.comments.dto.CreateCommentDto;
import ru.practicum.dto.ClientStatDto;
import ru.practicum.dto.GetStatDto;
import ru.practicum.event.EventRepository;
import ru.practicum.event.dto.*;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventMapper;
import ru.practicum.event.model.SortEvent;
import ru.practicum.event.model.State;
import ru.practicum.exceptionHandler.ConflictException;
import ru.practicum.exceptionHandler.NotFoundException;
import ru.practicum.requests.EventRequestRepository;
import ru.practicum.requests.dto.EventRequestsPatchDto;
import ru.practicum.requests.dto.FullRequestsDto;
import ru.practicum.requests.dto.StatusListRequestDto;
import ru.practicum.requests.model.EventRequest;
import ru.practicum.requests.model.Status;
import ru.practicum.user.UserRepository;
import ru.practicum.user.model.User;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;
    private final EventRequestRepository eventRequestRepository;
    private final StatisticsClient statisticsClient;


    @Override
    public List<FullEventResponseDto> getEventsByParamForAdmin(String text, Set<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, SortEvent sort, int from, int size) {
        PageRequest pageRequest = getPageRequest(from, size);
        if (sort == SortEvent.EVENT_DATE) pageRequest.withSort(Sort.by("eventDate"));
        Page<Event> eventsByParamPage;
        if (onlyAvailable) {
            eventsByParamPage = eventRepository.getAvailableEventsByParam(text, categories, paid, rangeStart, rangeEnd, pageRequest);
        } else {
            eventsByParamPage = eventRepository.getEventsByParam(text, categories, paid, rangeStart, rangeEnd, pageRequest);
        }
        List<Event> eventsByParam = eventsByParamPage.get().collect(Collectors.toList());
        var views = findViews(eventsByParam);
        var listDtoResponse = eventsByParam.stream().map(event -> {
            var responseDto = modelMapper.map(event, FullEventResponseDto.class);
            responseDto.setConfirmedRequests(eventRequestRepository.countByStatusConfirmed(event.getId()));
            responseDto.setViews(views.containsKey(event.getId()) ? views.get(event.getId()) : 0);
            return responseDto;
        }).collect(Collectors.toList());
        if (sort == SortEvent.EVENT_DATE) eventsByParam.sort(Comparator.comparing(Event::getEventDate));
        return listDtoResponse;
    }

    private PageRequest getPageRequest(int from, int size) {
        return PageRequest.of(from > 0 ? from / size : 0, size);
    }

    @Override
    public FullEventResponseDto updateEvent(long eventId, UpdateEventAdminRequest dto) {
        if (dto.getParticipantLimit() != null)
            if (dto.getParticipantLimit() == 0) dto.setParticipantLimit(null);

        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event dont found"));
        modelMapper.map(dto, event);
        if (dto.getStateAction() != null) {
            switch (dto.getStateAction()) {
                case PUBLISH_EVENT:
                    if (event.getState().equals(State.PENDING)) {
                        event.setState(State.PUBLISHED);
                        event.setPublishedOn(LocalDateTime.now());
                    } else
                        throw new ConflictException("Cannot publish the event because it's not in the right state: " + event.getState());
                    break;
                case REJECT_EVENT:
                    if (!event.getState().equals(State.PUBLISHED)) {
                        event.setState(State.CANCELED);
                    } else
                        throw new ConflictException("Cannot reject the event because it's not in the right state: " + event.getState());
            }
        }
        event = eventRepository.save(event);
        var responseDto = modelMapper.map(event, FullEventResponseDto.class);
        if (event.getState() == State.CANCELED) responseDto.setParticipantLimit(0);
        return responseDto;
    }

    @Override
    public List<EventShortDto> getEventsByParam(String text, Set<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                                LocalDateTime rangeEnd, boolean onlyAvailable, SortEvent sort,
                                                int from, int size, HttpServletRequest request) {
        PageRequest pageRequest = getPageRequest(from, size);
        if (sort == SortEvent.EVENT_DATE) pageRequest.withSort(Sort.by("eventDate"));

        Page<Event> eventsByParam;
        if (onlyAvailable) {
            eventsByParam = eventRepository.getAvailableEventsByParam(text, categories, paid, rangeStart, rangeEnd, pageRequest);
        } else {
            eventsByParam = eventRepository.getEventsByParam(text, categories, paid, rangeStart, rangeEnd, pageRequest);
        }
        addHit(request);
        return eventsByParam.get().map(event -> modelMapper.map(event, EventShortDto.class)).collect(Collectors.toList());
    }


    @Override
    public FullEventResponseDto getEventPublic(long eventId, HttpServletRequest request) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event dont found"));
        if (event.getState() != State.PUBLISHED) throw new NotFoundException("Event dont found");
        addHit(request);
        Map<Long, Long> views = findViews(List.of(event));
        FullEventResponseDto dto = modelMapper.map(event, FullEventResponseDto.class);
        dto.setViews(!views.containsKey(eventId) ? 0 : views.get(eventId));
        return dto;
    }

    @Override
    public FullEventResponseDto createEvent(EventCreateDto eventDto, Long userId) {
        User initiator = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User dont found"));

        Category category = categoryRepository.findById(eventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Category dont found"));

        Event event = EventMapper.createDtoToEvent(eventDto, initiator, category);
        event = eventRepository.save(event);
        log.info("State - " + event.getState());
        return EventMapper.toFullEventResponseDto(event, 0, 0);
    }

    @Override
    public List<EventShortDto> getEventsForUser(long userId, int from, int size) {
        PageRequest pageRequest = getPageRequest(from, size);
        User initiator = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User dont found"));
        return eventRepository.findAll(pageRequest).stream().map((x) ->
                EventMapper.toGetEventDto(x, initiator, 0, 0)).collect(Collectors.toList());
    }

    @Override
    public FullEventResponseDto getEventForUser(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User dont found"));

        Event event = eventRepository.findByInitiatorIdAndId(userId, eventId)
                .orElseThrow(() -> new NotFoundException("Not found event"));
        var responseDto = EventMapper.toFullEventResponseDto(event, 0, 0);
        if (event.getState() == State.CANCELED) responseDto.setParticipantLimit(0);
        return responseDto;
    }

    @Override
    public FullEventResponseDto updateEventForUser(Long userId, Long eventId, EventPatchUserDto eventDto) {
        if (eventDto.getParticipantLimit() != null)
            if (eventDto.getParticipantLimit() == 0)
                eventDto.setParticipantLimit(null);
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User dont found"));
        Event event = eventRepository.findByInitiatorIdAndId(userId, eventId)
                .orElseThrow(() -> new NotFoundException("Not found event"));
        if (event.getState() == State.PUBLISHED)
            throw new ConflictException("Only pending or canceled events can be changed");
        modelMapper.map(eventDto, event);
        event = eventRepository.save(event);
        if (eventDto.getStateAction() != null)
            switch (eventDto.getStateAction()) {
                case CANCEL_REVIEW:
                    event.setState(State.CANCELED);
                    break;
                case SEND_TO_REVIEW:
                    event.setState(State.PENDING);
                    break;
            }
        return EventMapper.toFullEventResponseDto(event, 0, 0);
    }

    @Override
    public List<FullRequestsDto> getRequestForEvent(Long userId, Long eventId) {
        return eventRequestRepository.findAllByEvent_InitiatorIdAndEventId(userId, eventId);
    }

    @Override
    public StatusListRequestDto changeStatusRequests(Long userId, Long eventId, EventRequestsPatchDto dto) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User dont found"));
        Event event = eventRepository.findByInitiatorIdAndId(userId, eventId)
                .orElseThrow(() -> new NotFoundException("Event dont found"));

        if (!event.getRequestModeration())
            return new StatusListRequestDto(eventRequestRepository.findConfirmDtoByIdIn(eventId, dto.getRequestIds()),
                    eventRequestRepository.findRejectedDtoByIdIn(eventId, dto.getRequestIds()));

        int countConfirmed = eventRequestRepository.countByStatusConfirmed(eventId);

        List<EventRequest> eventRequests = eventRequestRepository.findByIdIn(eventId, dto.getRequestIds());

        if (countConfirmed >= event.getParticipantLimit())
            throw new ConflictException("The participant limit has been reached");

        for (var eventRequest : eventRequests) {
            if (eventRequest.getStatus() != Status.PENDING)
                throw new ConflictException("Status can be changed only for requests that are in the pending state");
            if (countConfirmed < event.getParticipantLimit() || event.getParticipantLimit() == 0) {
                eventRequest.setStatus(dto.getStatus());
                if (dto.getStatus() == Status.CONFIRMED)
                    countConfirmed++;
            } else eventRequest.setStatus(Status.REJECTED);
            eventRequestRepository.save(eventRequest);
            log.info("Request {} status changed to {}", eventRequest.getId(), eventRequest.getStatus());
        }
        return new StatusListRequestDto(eventRequestRepository.findConfirmDtoByIdIn(eventId, dto.getRequestIds()),
                eventRequestRepository.findRejectedDtoByIdIn(eventId, dto.getRequestIds()));
    }

    @Override
    public CommentDtoResponse createComment(Long userId, Long eventId, Long commenterId, CreateCommentDto createCommentDto) {
        User commenter = userRepository.findById(commenterId).orElseThrow(() ->
                new NotFoundException("User (Commenter) dont found"));
        Event event = eventRepository.findByInitiatorIdAndId(userId, eventId)
                .orElseThrow(() -> new NotFoundException("Event id " + eventId + " dont found"));

        Comment comment = CommentMapper.toComment(commenter, event, createCommentDto);
        comment = commentRepository.save(comment);
        return CommentMapper.toCommentDtoResponse(comment);
    }

    @Override
    public CommentDtoResponse updateComment(Long userId, Long eventId, Long commentId, Long commenterId, CreateCommentDto commentDto) {
        Comment comment = commentRepository.findByIdAndCommenterIdAndEventId(commentId, commenterId, eventId)
                .orElseThrow(() -> new NotFoundException("Comment " + commentId + " dont find"));
        comment.setText(commentDto.getText());
        commentRepository.save(comment);
        return CommentMapper.toCommentDtoResponse(comment);
    }

    @Override
    public List<CommentDtoResponse> getCommentsByParam(Long eventId, int from, int size) {
        eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event dont found"));
        PageRequest pageRequest = getPageRequest(from, size);
        Page<Comment> page = commentRepository.findAllByEventId(eventId, pageRequest);
        return page.get().map(CommentMapper::toCommentDtoResponse).collect(Collectors.toList());
    }

    @Override
    public void deleteCommentByAdmin(long eventId, Long commentId) {
        commentRepository.findByEventIdAndId(eventId, commentId).ifPresentOrElse(commentRepository::delete, () -> {
            throw new NotFoundException("Comment dont found");
        });
    }

    private void addHit(HttpServletRequest request) {
        statisticsClient.addHit("ewm-main-service", request.getRequestURI(), request);
    }

    private Map<Long, Long> findViews(List<Event> events) {

        ClientStatDto clientStatDto = ClientStatDto.builder().unique(true)
                .uris(events.stream().map(Event::getUri).collect(Collectors.toList())).build();
        log.info("clientStatDto -" + clientStatDto);
        List<GetStatDto> dtoStatList;
        try {
            dtoStatList = statisticsClient.getStaticsForUri(clientStatDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Map<Long, Long> map = new HashMap<>();
        for (var dto : dtoStatList) {
            String[] parts = dto.getUri().split("/");
            Long eventId = parts.length < 2 ? 0 : Long.parseLong(parts[2]);
            map.put(eventId, dto.getHits());
            log.info("Full uri -" + dto.getUri() + " key - " + eventId + " val - " + dto.getHits());
        }
        return map;
    }
}
