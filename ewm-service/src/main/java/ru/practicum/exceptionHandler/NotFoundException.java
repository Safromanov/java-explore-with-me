package ru.practicum.exceptionHandler;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String str) {
        super(str);
    }
}
