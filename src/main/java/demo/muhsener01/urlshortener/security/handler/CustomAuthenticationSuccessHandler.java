package demo.muhsener01.urlshortener.security.handler;

import demo.muhsener01.urlshortener.io.response.OperationResponse;
import demo.muhsener01.urlshortener.security.JwtUtils;
import demo.muhsener01.urlshortener.security.SecurityConstants;
import demo.muhsener01.urlshortener.security.UserPrincipal;
import demo.muhsener01.urlshortener.utils.JsonUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final SecurityConstants securityConstants;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        UserPrincipal userPrincipal = ((UserPrincipal) authentication.getPrincipal());
        log.info("User with id: '{}' username: '{}' logged in successfully", userPrincipal.getId(), userPrincipal.getUsername());

        String token = JwtUtils.generateToken(userPrincipal.getId(), userPrincipal.getEmail(), userPrincipal.getUsername(), userPrincipal.getRoles());

        response.addHeader(securityConstants.getHeaderName(), securityConstants.getTokenPrefix() + token);
        response.setStatus(200);
        OperationResponse operationResponse = new OperationResponse("LOGIN", true);
        String body = JsonUtils.convertToJson(operationResponse);

        response.setContentType("application/json");
        response.getWriter().append(body);


    }
}
