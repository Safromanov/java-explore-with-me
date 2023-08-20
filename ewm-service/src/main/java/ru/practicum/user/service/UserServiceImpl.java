package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.exceptionHandler.ConflictException;
import ru.practicum.exceptionHandler.NotFoundException;
import ru.practicum.user.UserRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.model.dto.CreateUserDto;
import ru.practicum.user.model.dto.UserDto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Override
    public List<UserDto> getUsersByParam(Set<Long> ids, int from, int size) {
        PageRequest pageRequest = getPageRequest(from, size);
        return userRepository.findByIdIn(ids, pageRequest).get()
                .map(x -> modelMapper.map(x, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto createUser(CreateUserDto createUserDto) {
        User user;
        try {
            user = userRepository.save(modelMapper.map(createUserDto, User.class));
        } catch (Exception e) {
            throw new ConflictException("Name or email already exist");
        }

        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public void deleteUser(long id) {
        userRepository.findById(id).ifPresentOrElse(userRepository::delete, () -> {
            throw new NotFoundException("User dont found");
        });
    }

    private PageRequest getPageRequest(int from, int size) {
        return PageRequest.of(from > 0 ? from / size : 0, size);
    }
}
