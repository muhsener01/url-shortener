package demo.muhsener01.urlshortener.io.handler;

import demo.muhsener01.urlshortener.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler({UserAlreadyExistsException.class, InvalidUrlException.class})
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(Exception e, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), 400, request.getRequestURI(), e.getMessage());

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(AuthenticationRequiredException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationRequiredException(AuthenticationRequiredException exception, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), 401, request.getRequestURI(), exception.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(NotResolvableException.class)
    public ResponseEntity<ErrorResponse> handleNotResolvableException(NotResolvableException exception, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), 400, request.getRequestURI(), exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


    @ExceptionHandler({TextNotFoundException.class, NoSuchUrlFoundException.class})
    public ResponseEntity<ErrorResponse> handleTextNotFoundException(NotFoundException exception, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), 404, request.getRequestURI(), exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @Data
    @AllArgsConstructor
    public static final class ErrorResponse {
        private LocalDateTime timestamp;
        private int status;
        private String path;
        private String message;


    }
}
