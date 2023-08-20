package ru.practicum.event.service;

import ru.practicum.comments.dto.CommentDtoResponse;
import ru.practicum.comments.dto.CreateCommentDto;
import ru.practicum.event.dto.*;
import ru.practicum.event.model.SortEvent;
import ru.practicum.requests.dto.EventRequestsPatchDto;
import ru.practicum.requests.dto.FullRequestsDto;
import ru.practicum.requests.dto.StatusListRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventService {

    List<FullEventResponseDto> getEventsByParamForAdmin(String text, Set<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, SortEvent sort, int from, int size);

    FullEventResponseDto updateEvent(long eventId, UpdateEventAdminRequest dto);

    List<EventShortDto> getEventsByParam(String text, Set<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd, boolean onlyAvailable, SortEvent sort,
                                         int from, int size, HttpServletRequest request);

    FullEventResponseDto getEventPublic(long eventId, HttpServletRequest request);

    FullEventResponseDto createEvent(EventCreateDto eventDto, Long userId);

    List<EventShortDto> getEventsForUser(long userId, int from, int size);

    FullEventResponseDto getEventForUser(Long userId, Long eventId);

    FullEventResponseDto updateEventForUser(Long userId, Long eventId, EventPatchUserDto eventDto);

    List<FullRequestsDto> getRequestForEvent(Long userId, Long eventId);

    StatusListRequestDto changeStatusRequests(Long userId, Long eventId, EventRequestsPatchDto dto);

    CommentDtoResponse createComment(Long userId, Long eventId, Long commenterId,CreateCommentDto comment);

    CommentDtoResponse updateComment(Long userId, Long eventId, Long commentId, Long commenterId, CreateCommentDto comment);

    List<CommentDtoResponse> getCommentsByParam(Long eventId, int from, int size);

    void deleteCommentByAdmin(long eventId, Long commentId);
}
