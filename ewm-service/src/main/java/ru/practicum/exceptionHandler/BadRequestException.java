package ru.practicum.exceptionHandler;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String s) {
        super(s);
    }
}
