package demo.muhsener01.urlshortener.service;

import demo.muhsener01.urlshortener.domain.entity.feedback.Feedback;
import demo.muhsener01.urlshortener.shared.dto.FeedbackResponse;
import demo.muhsener01.urlshortener.shared.dto.GetAllFeedbackResponse;

import java.util.UUID;

public interface FeedbackService {

    FeedbackResponse save(String title, String content);

    Feedback findById(UUID feedbackId);

    GetAllFeedbackResponse findAll(int page, int limit);
}
