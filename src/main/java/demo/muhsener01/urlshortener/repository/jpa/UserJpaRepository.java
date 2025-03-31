package demo.muhsener01.urlshortener.repository.jpa;

import demo.muhsener01.urlshortener.domain.entity.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserJpaRepository extends JpaRepository<MyUser, UUID> {


    boolean existsByUsernameOrEmail(String username, String email);


    @Query("SELECT u from MyUser u LEFT JOIN FETCH u.roles WHERE u.username = :username")
    Optional<MyUser> findByUsername(String username);
}
