package ru.practicum.exceptionHandler;

public class ConflictException extends RuntimeException {

    public ConflictException(String str) {
        super(str);
    }
}
