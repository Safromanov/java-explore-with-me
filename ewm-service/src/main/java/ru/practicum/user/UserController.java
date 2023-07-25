package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.model.dto.CreateUserDto;
import ru.practicum.user.model.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getUsers(@RequestParam(required = false) Set<Long> ids,
                                  @RequestParam(defaultValue = "0") int from,
                                  @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        log.debug("GET /admin/users");
        return userService.getUsers(ids, from, size);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto postUser(@RequestBody @Valid CreateUserDto userDto) {
        log.debug("POST /admin/users with dto: {}.", userDto);
        return userService.postUser(userDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable long id) {
        log.debug("DELETE /admin/users with id: {}.", id);
        userService.deleteUser(id);
    }
}
