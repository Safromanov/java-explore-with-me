package ru.practicum.user.service;

import ru.practicum.user.model.dto.CreateUserDto;
import ru.practicum.user.model.dto.UserDto;

import java.util.List;
import java.util.Set;

public interface UserService {
    List<UserDto> getUsersByParam(Set<Long> ids, int from, int size);

    UserDto createUser(CreateUserDto createUserDto);

    void deleteUser(long id);
}
