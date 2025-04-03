package demo.muhsener01.urlshortener.security.handler;

import demo.muhsener01.urlshortener.io.handler.GlobalExceptionHandler;
import demo.muhsener01.urlshortener.utils.JsonUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.time.LocalDateTime;

public class CustomAuthFailureHandler implements AuthenticationFailureHandler {


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        GlobalExceptionHandler.ErrorResponse errorResponse =
                new GlobalExceptionHandler.ErrorResponse(LocalDateTime.now(), 401, request.getRequestURI(), exception.getMessage());


        if (exception instanceof InternalAuthenticationServiceException internalAuthenticationServiceException) {
            errorResponse.setStatus(500);
            errorResponse.setMessage("Internal server error!");
            response.setStatus(500);
        } else {
            response.setStatus(401);
        }

        String body = JsonUtils.convertToJson(errorResponse);
        response.setContentType("application/json");
        response.getWriter().append(body);
    }
}
