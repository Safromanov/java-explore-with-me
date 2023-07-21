package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.category.NotFoundException;
import ru.practicum.user.model.User;
import ru.practicum.user.model.UserRepository;
import ru.practicum.user.model.dto.CreateUserDto;
import ru.practicum.user.model.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    public List<UserDto> getUsers(int from,
                                  int size) {
        PageRequest pageRequest = getPageRequest(from, size);
        return userRepository.findAll(pageRequest).get()
                .map(x -> modelMapper.map(x, UserDto.class))
                .collect(Collectors.toList());
    }

    public UserDto postUser(CreateUserDto createUserDto) {
        User user = userRepository.save(modelMapper.map(createUserDto, User.class));
        return modelMapper.map(user, UserDto.class);
    }

    public void deleteUser(long id) {
        userRepository.findById(id).ifPresentOrElse(userRepository::delete, () -> {
            throw new NotFoundException("User dont found");
        });
    }

    private PageRequest getPageRequest(int from, int size) {
        return PageRequest.of(from > 0 ? from / size : 0, size);
    }
}
