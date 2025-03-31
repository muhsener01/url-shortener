package demo.muhsener01.urlshortener.repository;

import demo.muhsener01.urlshortener.domain.entity.MyUser;

import java.util.Optional;

public interface UserRepository {

    boolean existsByUsernameOrEmail(String username, String email);

    MyUser save(MyUser user);

    Optional<MyUser> findByUsername(String username);
}
