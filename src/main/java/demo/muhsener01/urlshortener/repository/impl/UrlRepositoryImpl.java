package demo.muhsener01.urlshortener.repository.impl;

import demo.muhsener01.urlshortener.domain.entity.Link;
import demo.muhsener01.urlshortener.repository.UrlRepository;
import demo.muhsener01.urlshortener.repository.jpa.UrlJpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("urlRepository")
@Slf4j
@RequiredArgsConstructor
public class UrlRepositoryImpl implements UrlRepository {

    private final UrlJpaRepository urlJpaRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Link save(Link url) {
        entityManager.persist(url);
        entityManager.flush(); // To trigger db exceptions

        return url;
    }

    @Override
    @Transactional
    @Retryable(retryFor = ConstraintViolationException.class)
    public Link generateUniqueKeyAndSave(Link link) {
        String uniqueKey = hash(link.getContent() + UUID.randomUUID()).substring(0, 7);
//        String uniqueKey = "forTest";
        link.setId(uniqueKey);

        try {
            entityManager.persist(link);
            entityManager.flush();
            return link;
        } catch (ConstraintViolationException e) {
            log.info("Duplicated url code is found: {} and retrying again...", uniqueKey);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Link> findById(String id) {
        return Optional.ofNullable(entityManager.find(Link.class, id));
    }

    @Override
    @Transactional
    public Link update(Link url) {
        entityManager.merge(url);
        return url;
    }

    @Override
    public boolean existsById(String id) {
        return urlJpaRepository.existsById(id);
    }

    @Override
    public Optional<Link> findByIdIfNotRemoved(String id) {
        return urlJpaRepository.findByIdIfNotRemoved(id);
    }

    @Override
    public List<Link> findAllByUserIdIfNotRemoved(UUID authenticatedUserId, int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);

        return urlJpaRepository.findAllByUserIdIfNotRemoved(authenticatedUserId, pageable);
    }

    private String hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
