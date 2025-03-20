package demo.muhsener01.urlshortener.service;

import demo.muhsener01.urlshortener.command.SignUpCommand;
import demo.muhsener01.urlshortener.command.response.SignUpResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    SignUpResponse createUser(SignUpCommand command);
}
