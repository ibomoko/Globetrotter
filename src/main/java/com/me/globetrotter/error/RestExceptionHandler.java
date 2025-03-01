package com.me.globetrotter.error;

import com.me.globetrotter.error.exception.AuthenticationException;
import com.me.globetrotter.error.exception.AuthorizationException;
import com.me.globetrotter.error.exception.ResourceAlreadyExistsException;
import com.me.globetrotter.error.exception.ResourceNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;



@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException e) {
        StringBuilder stringBuilder = new StringBuilder();
        e.getBindingResult()
                .getAllErrors()
                .forEach(error -> stringBuilder.append(String.format("%s : %s ",
                        ((FieldError) error).getField(), error.getDefaultMessage())));

        return buildErrorResponse(stringBuilder.toString(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(Exception e) {
        return buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(Exception e) {
        return buildErrorResponse(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleResourceAlreadyExistsException(Exception e) {
        return buildErrorResponse(e.getMessage(), HttpStatus.CONFLICT);
    }


    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationException(Exception e){
        return buildErrorResponse(e.getMessage(), HttpStatus.FORBIDDEN);
    }


    public static ResponseEntity<ErrorResponse> buildErrorResponse(String message, HttpStatus status) {
        return new ResponseEntity<>(new ErrorResponse(status, message), status);
    }

    public static ResponseEntity<ErrorResponse> buildErrorResponse(List<String> messages, HttpStatus status) {
        return new ResponseEntity<>(new ErrorResponse(status, messages), status);
    }




}
