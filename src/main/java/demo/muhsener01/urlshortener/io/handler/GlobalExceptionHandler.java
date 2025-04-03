package demo.muhsener01.urlshortener.io.handler;

import demo.muhsener01.urlshortener.exception.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringJoiner;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    private void logError(Exception e) {
        log.debug("[ERROR]: {}", e.getMessage(), e);
        log.error("[ERROR]: {}", e.getMessage());
    }


    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException e, HttpServletRequest request) {
        logError(e);
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), 409, request.getRequestURI(), e.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }


    @ExceptionHandler({InvalidUrlException.class, InvalidDomainException.class, IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> handleInvalidUrlException(Exception e, HttpServletRequest request) {
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


    @ExceptionHandler({TextNotFoundException.class, URLNotFoundException.class, RoleNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleTextNotFoundException(NotFoundException exception, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), 404, request.getRequestURI(), exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletRequest request) {
//        logError(exception);

        Map<String, Object> body = new LinkedHashMap<>();
        String errorTemplateMessage = "%s: '%s'";
        StringJoiner stringJoiner = new StringJoiner(", ");
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            stringJoiner.add(errorTemplateMessage.formatted(fieldError.getField(), fieldError.getField().equals("password") ? "****" : fieldError.getRejectedValue()));
            body.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        log.error("INVALID INPUT ERROR: {} ", stringJoiner.toString());

        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), 400, request.getRequestURI(), "Invalid inputs.", body);
        return ResponseEntity.badRequest().body(errorResponse);

    }

    @ExceptionHandler({AuthorizationDeniedException.class, NoPermissionException.class})
    public ResponseEntity<ErrorResponse> handleAuthorizationDeniedException(Exception exception, HttpServletRequest request) {


        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), 403, request.getRequestURI(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);

    }


    @Data
//    @AllArgsConstructor
    public static final class ErrorResponse {
        @Schema(example = "2023-08-10T12:34:56")
        private LocalDateTime timestamp;
        @Schema(example = "400")
        private int status;
        @Schema(example = "/api/v1/auth/signup")
        private String path;
        @Schema(example = "Invalid inputs.")
        private String message;
        @Schema(example = "{\"email\":\"Invalid email format.\",\"username\":\"Username must be between 3 and 20 charaters.\",\"password\":\"Password must be between 6 and 50 characters.\"}")
        private Object details;

        public ErrorResponse(LocalDateTime timestamp, int status, String path, String message) {
            this.timestamp = timestamp;
            this.status = status;
            this.path = path;
            this.message = message;
        }

        public ErrorResponse(LocalDateTime timestamp, int status, String path, String message, Object details) {
            this.timestamp = timestamp;
            this.status = status;
            this.path = path;
            this.message = message;
            this.details = details;
        }
    }
}
