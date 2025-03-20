package demo.muhsener01.urlshortener.service.impl;

import demo.muhsener01.urlshortener.command.SignUpCommand;
import demo.muhsener01.urlshortener.command.response.SignUpResponse;
import demo.muhsener01.urlshortener.domain.entity.MyUser;
import demo.muhsener01.urlshortener.domain.entity.Role;
import demo.muhsener01.urlshortener.domain.entity.RoleName;
import demo.muhsener01.urlshortener.exception.UserAlreadyExistsException;
import demo.muhsener01.urlshortener.repository.RoleRepository;
import demo.muhsener01.urlshortener.repository.UserRepository;
import demo.muhsener01.urlshortener.security.UserPrincipal;
import demo.muhsener01.urlshortener.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public SignUpResponse createUser(SignUpCommand command) {
        String username = command.getUsername();
        String email = command.getEmail();
        checkUserUniqueness(username, email);

        Role userRole = fetchUserRole();

        MyUser myUser = MyUser.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(command.getPassword()))
                .roles(List.of(userRole))
                .build();


        MyUser savedUser = userRepository.save(myUser);
        return new SignUpResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
    }


    private void checkUserUniqueness(String username, String email) {
        if (userRepository.existsByUsernameOrEmail(username, email))
            throw new UserAlreadyExistsException(username, email);
    }

    private Role fetchUserRole() {
        return roleRepository.findById(RoleName.USER.name()).orElseThrow(() ->
                new IllegalStateException("User role not found!!"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUser myUser = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("No such user found with provided username: " + username)
        );

        return new UserPrincipal(myUser);
    }
}
