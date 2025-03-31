package demo.muhsener01.urlshortener.bootstrap;

import demo.muhsener01.urlshortener.domain.entity.Role;
import demo.muhsener01.urlshortener.domain.enums.RoleName;
import demo.muhsener01.urlshortener.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoleInitializer {

    private final RoleRepository roleRepository;

    @EventListener
    public void onApplicationReady(ApplicationReadyEvent event) {
        Optional<Role> userRole = roleRepository.findById(RoleName.USER.name());
        Optional<Role> adminRole = roleRepository.findById(RoleName.ADMIN.name());

        if (userRole.isEmpty()){
            roleRepository.save(new Role(RoleName.USER));
            log.trace("User Role persisted into database.");

        }

        if (adminRole.isEmpty()) {
            roleRepository.save(new Role(RoleName.ADMIN));
            log.trace("Admin Role persisted into database.");
        }


    }
}
