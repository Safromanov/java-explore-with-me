package ru.practicum.exceptionHandler.errorResponse;

import com.fasterxml.jackson.annotation.JsonValue;

public enum StatusExceptionsEnum {
    BAD_REQUEST("400 BAD_REQUEST"), NOT_FOUND("404 NOT_FOUND"), FORBIDDEN("409 CONFLICT");

    private final String status;

    StatusExceptionsEnum(String status) {
        this.status = status;
    }

    @JsonValue
    public String getStatus() {
        return status;
    }
}
