package demo.muhsener01.urlshortener.repository.jpa;

import demo.muhsener01.urlshortener.domain.entity.Link;
import demo.muhsener01.urlshortener.domain.enums.LinkStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UrlJpaRepository extends JpaRepository<Link, String> {


    List<Link> findAllByUserId(UUID userId, Pageable pageable);

    boolean existsByIdAndStatus(String id, LinkStatus status);

    @Query("SELECT u from Link u WHERE u.id = :id AND u.status != 'REMOVED'")
    Optional<Link> findByIdIfNotRemoved(String id);

    @Query("SELECT u from Link u WHERE u.userId= :userId AND u.status != 'REMOVED' ORDER BY u.createdAt DESC")
    List<Link> findAllByUserIdIfNotRemoved(UUID userId, Pageable pageable);
}
