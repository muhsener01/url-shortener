package demo.muhsener01.urlshortener.security.handler;

import demo.muhsener01.urlshortener.io.handler.GlobalExceptionHandler;
import demo.muhsener01.urlshortener.utils.JsonUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.time.LocalDateTime;

public class CustomAuthFailureHandler implements AuthenticationFailureHandler {


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        GlobalExceptionHandler.ErrorResponse errorResponse =
                new GlobalExceptionHandler.ErrorResponse(LocalDateTime.now(), 401, request.getRequestURI(), exception.getMessage());


        String body = JsonUtils.convertToJson(errorResponse);
        response.setStatus(401);
        response.setContentType("application/json");
        response.getWriter().append(body);
    }
}
