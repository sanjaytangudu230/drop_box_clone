package com.drop_box_clone.exception;

import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DropBoxException extends RuntimeException{
    private final HttpStatus status;
    private final String message;

    public DropBoxException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
