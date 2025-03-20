package demo.muhsener01.urlshortener.repository;

import demo.muhsener01.urlshortener.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
}
