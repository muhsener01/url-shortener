package demo.muhsener01.urlshortener.repository;

import demo.muhsener01.urlshortener.domain.entity.MyUser;
import demo.muhsener01.urlshortener.exception.DataAccessException;

import java.util.Optional;

public interface UserRepository {

    boolean existsByUsernameOrEmail(String username, String email) throws DataAccessException;

    MyUser save(MyUser user) throws DataAccessException;

    Optional<MyUser> findByUsername(String username) throws DataAccessException;
}
