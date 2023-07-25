package ru.practicum.exceptionHandler.errorResponse;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class ErrorResponse {
    private final StatusExceptionsEnum status;

    private final String reason;
    private final String message;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timestamp;

    public ErrorResponse(StatusExceptionsEnum status, String message) {
        this.status = status;
        switch (status) {
            case BAD_REQUEST:
                reason = "Incorrectly made request.";
                break;
            case NOT_FOUND:
                reason = "The required object was not found.";
                break;
            case FORBIDDEN:
                reason = "For the requested operation the conditions are not met.";
                break;
            default:
                reason = "Unknown status";
        }


        this.message = message;
        this.timestamp = LocalDateTime.now();
    }


}
