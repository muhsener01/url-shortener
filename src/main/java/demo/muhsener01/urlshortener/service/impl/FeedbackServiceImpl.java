package demo.muhsener01.urlshortener.service.impl;

import demo.muhsener01.urlshortener.domain.entity.feedback.Feedback;
import demo.muhsener01.urlshortener.exception.FeedbackNotFoundException;
import demo.muhsener01.urlshortener.shared.dto.FeedbackResponse;
import demo.muhsener01.urlshortener.shared.dto.GetAllFeedbackResponse;
import demo.muhsener01.urlshortener.mapper.FeedbackMapper;
import demo.muhsener01.urlshortener.repository.FeedbackJpaRepository;
import demo.muhsener01.urlshortener.service.FeedbackService;
import demo.muhsener01.urlshortener.service.SecurityOperations;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackJpaRepository feedbackJpaRepository;
    private final SecurityOperations securityOperations;
    private final FeedbackMapper feedbackMapper;

    @Override
    @Transactional
    public FeedbackResponse save(String title, String content) {
        UUID userId = securityOperations.getAuthenticatedUserId();

        Feedback feedback = new Feedback(userId, title, content);
        feedbackJpaRepository.save(feedback);


        return new FeedbackResponse(feedback.getId(), feedback.getUserId(), feedback.getTitle(), feedback.getContent());

    }

    @Override
    @Transactional(readOnly = true)
    public Feedback findById(UUID feedbackId) {
        securityOperations.validateIsAdmin();

        return feedbackJpaRepository.findById(feedbackId).orElseThrow(() -> new FeedbackNotFoundException("id", feedbackId));
    }


    @Transactional(readOnly = true)
    public GetAllFeedbackResponse findAll(int page, int limit) {
        securityOperations.validateIsAdmin();

        Pageable pageable = PageRequest.of(page, limit);
        Page<Feedback> result = feedbackJpaRepository.findAllByOrderCreatedAtDesc(pageable);

        return GetAllFeedbackResponse.builder()
                .totalPages(result.getTotalPages())
                .totalRecords(result.getTotalElements())
                .page(page)
                .limit(limit)
                .data(result.getContent().stream().map(feedbackMapper::toResponse).toList())
                .build();


    }
}
