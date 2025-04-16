package demo.muhsener01.urlshortener.mapper;

import demo.muhsener01.urlshortener.domain.entity.MyUser;
import demo.muhsener01.urlshortener.domain.entity.feedback.Feedback;
import demo.muhsener01.urlshortener.shared.dto.FeedbackResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FeedbackMapper {

    public FeedbackResponse toResponse(Feedback feedback) {
        MyUser user = feedback.getUser();
        return new FeedbackResponse(feedback.getId(), feedback.getUserId(), feedback.getContent(), feedback.getTitle(), user.getUsername(), user.getEmail());
    }


    public List<FeedbackResponse> toResponse(List<Feedback> feedbacks) {
        return feedbacks.stream().map(this::toResponse).toList();
    }
}
