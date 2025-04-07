package demo.muhsener01.urlshortener.repository.impl;

import demo.muhsener01.urlshortener.domain.entity.MyUser;
import demo.muhsener01.urlshortener.exception.DataAccessException;
import demo.muhsener01.urlshortener.repository.UserRepository;
import demo.muhsener01.urlshortener.repository.jpa.UserJpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;


    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsernameOrEmail(String username, String email) {
        return userJpaRepository.existsByUsernameOrEmail(username, email);
    }

    @Override
    @Transactional
    public MyUser save(MyUser user) throws DataAccessException {
        try {
            entityManager.persist(user);
            entityManager.flush();
            return user;
        } catch (Exception e) {
            String message = "Error while saving user with ID: '%s' due to : '%s' ".formatted(user.getId(), e.getMessage());
            log.debug(message, e);
            throw new DataAccessException(message, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MyUser> findByUsername(String username) {
        return userJpaRepository.findByUsername(username);
    }
}
