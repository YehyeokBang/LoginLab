package com.example.loginlab.common.error.handler;

import com.example.loginlab.common.error.exception.CustomException;
import com.example.loginlab.common.error.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e){
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }

}
