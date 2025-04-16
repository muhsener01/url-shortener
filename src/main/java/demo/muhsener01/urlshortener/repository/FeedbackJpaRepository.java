package demo.muhsener01.urlshortener.repository;

import demo.muhsener01.urlshortener.domain.entity.feedback.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FeedbackJpaRepository extends JpaRepository<Feedback, UUID> {

    @Query("SELECT f from Feedback f LEFT JOIN FETCH f.user WHERE f.id = :feedbackId")
    Optional<Feedback> findById(UUID feedbackId);

    @Query("SELECT f from Feedback f LEFT JOIN FETCH f.user ORDER BY f.createdAt DESC")
    Page<Feedback> findAllByOrderCreatedAtDesc(Pageable pageable);
}
