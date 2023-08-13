package ru.practicum.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.stream.Collectors;
@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotFoundExceptionException(IllegalArgumentException e) {
        log.warn(e.getMessage() + "\n" + Arrays.stream(e.getStackTrace()).map(s -> s.toString()).collect(Collectors.joining("\n")));
        return new ErrorResponse(StatusExceptionsEnum.BAD_REQUEST, e.getMessage());
    }
}
