package demo.muhsener01.urlshortener.bootstrap;

import demo.muhsener01.urlshortener.domain.entity.MyUser;
import demo.muhsener01.urlshortener.domain.entity.Role;
import demo.muhsener01.urlshortener.domain.enums.RoleName;
import demo.muhsener01.urlshortener.repository.RoleRepository;
import demo.muhsener01.urlshortener.repository.UserRepository;
import demo.muhsener01.urlshortener.security.SecurityConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final SecurityConstants securityConstants;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady(ApplicationReadyEvent event) {
        Optional<Role> userRole = roleRepository.findById(RoleName.USER.name());
        Optional<Role> adminRole = roleRepository.findById(RoleName.ADMIN.name());

        Role roleUser;
        if (userRole.isEmpty()) {
            roleUser = roleRepository.save(new Role(RoleName.USER));
            log.info("User Role persisted into database.");
        } else
            roleUser = userRole.get();

        Role roleAdmin;
        if (adminRole.isEmpty()) {
            roleAdmin = roleRepository.save(new Role(RoleName.ADMIN));
            log.info("Admin Role persisted into database.");
        } else
            roleAdmin = adminRole.get();


        userRepository.findByUsername(securityConstants.getAdminUsername()).ifPresentOrElse(
                admin -> log.info("Admin with username '{}' started.", admin.getUsername()),
                () -> {
                    MyUser admin = MyUser.builder()
                            .username(securityConstants.getAdminUsername())
                            .email(securityConstants.getAdminEmail())
                            .password(securityConstants.getAdminPassword())
                            .roles(List.of(roleAdmin, roleUser))
                            .password(passwordEncoder.encode(securityConstants.getAdminPassword()))
                            .build();

                    userRepository.save(admin);
                    log.info("Admin persisted into database.");
                }
        );


        log.info("Application is ready.");


    }
}
