package ru.practicum.exceptionHandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exceptionHandler.errorResponse.ErrorResponse;
import ru.practicum.exceptionHandler.errorResponse.StatusExceptionsEnum;
import ru.practicum.exceptionHandler.errorResponse.ValidationErrorResponse;
import ru.practicum.exceptionHandler.errorResponse.Violation;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictExceptionException(ConflictException e) {
        log.warn(e.getMessage());
        return new ErrorResponse(StatusExceptionsEnum.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundExceptionException(NotFoundException e) {
        log.warn(e.getMessage());
        return new ErrorResponse(StatusExceptionsEnum.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotBadRequestException(BadRequestException e) {
        log.warn(e.getMessage());
        return new ErrorResponse(StatusExceptionsEnum.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e
    ) {
        final List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> {
                    log.warn("{}: {} ({})", error.getField(), error.getDefaultMessage(), e.getClass().getSimpleName());
                    return new Violation(error.getField(), error.getDefaultMessage());
                })
                .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleConstraintValidationException(
            ConstraintViolationException e
    ) {
        final List<Violation> violations = e.getConstraintViolations().stream()
                .map(
                        violation -> {
                            log.warn("{}: {} ({})", violation.getPropertyPath(), violation.getMessage(), e.getClass().getSimpleName());
                            return new Violation(violation.getPropertyPath().toString(),
                                    violation.getMessage());
                        }
                )
                .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }
}

