package com.drop_box_clone.exception;

import com.drop_box_clone.dto.responses.BaseApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestControllerAdvice
public class DropBoxExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<?> handleDropBoxException (DropBoxException ex, HttpServletResponse response) {
        response.setStatus(ex.getStatus().value());
        return new ResponseEntity<>(BaseApiResponse.errorResponse(ex.getMessage()), ex.getStatus());
    }
}
