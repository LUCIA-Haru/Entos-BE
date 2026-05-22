package com.lr.entos.api.config;

import com.lr.entos.shared.exception.*;
import com.lr.entos.shared.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR = "ERROR";

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Object>> handleAuthenticationException(AuthenticationException ex){
        log.warn("AuthenticationException: {}", ex.getMessage());
        ApiResponse<Object> response = new ApiResponse<>(
                ERROR,
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized: Please log in again.",
                LocalDateTime.now(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDeniedException(AccessDeniedException ex){
        log.warn("AccessDeniedException: {}", ex.getMessage());
        ApiResponse<Object> response = new ApiResponse<>(
                ERROR,
                HttpStatus.FORBIDDEN.value(),
                "Permission Denied: You do not have the required authority.",
                LocalDateTime.now(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    //Handle validation errors (From @Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationErrors(MethodArgumentNotValidException ex){
        Map<String,String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(),error.getDefaultMessage()));
        ApiResponse<Object> response = new ApiResponse<>(
                ERROR,
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                LocalDateTime.now(),
                errors
        );
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserNotFoundException(
            UserNotFoundException ex
    ){
        log.warn("UserNotFoundException: {}",ex.getMessage());
        ApiResponse<Object> response = new ApiResponse<>(
                ERROR,
                HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }



    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserAlreadyExistsException(
            UserAlreadyExistsException ex
    ){
        log.warn("UserAlreadyExistsException: {}",ex.getMessage());
        ApiResponse<Object> response = new ApiResponse<>(
                ERROR,
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WrongPasswordException.class)
    public ResponseEntity<ApiResponse<Object>> handleWrongPasswerdException(
            WrongPasswordException ex
    ){
        log.warn("WrongPasswordException: {}",ex.getMessage());
        ApiResponse<Object> response = new ApiResponse<>(
                ERROR,
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleRoleNotFoundException(
            RoleNotFoundException ex
    ){
        log.warn("RoleNotFoundException: {}",ex.getMessage());
        ApiResponse<Object> response = new ApiResponse<>(
                ERROR,
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExistedRoleException.class)
    public ResponseEntity<ApiResponse<Object>> handleExistedRoleException(
            ExistedRoleException ex
    ){
        log.warn("ExistedRoleException: {}",ex.getMessage());
        ApiResponse<Object> response = new ApiResponse<>(
                ERROR,
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException ex){
        log.warn(ex.getMessage());
        ApiResponse<Object> response = new ApiResponse<>(
                ERROR,
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                null
        );
        return  new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleRunTimeException(RuntimeException ex){
        log.error("Runtime exception: {}", ex.getMessage(), ex);
        ApiResponse<Object> response = new ApiResponse<>(
                ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiResponse<Object>> handleUnknownException(Throwable ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        ApiResponse<Object> response = new ApiResponse<>(
                ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
