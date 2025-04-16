package demo.muhsener01.urlshortener.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackResponse {
    private UUID feedbackId;
    private UUID userId;
    private String content;
    private String title;
    private String username;
    private String email;


    public FeedbackResponse(UUID feedbackId, UUID userId, String content, String title) {
        this.feedbackId = feedbackId;
        this.userId = userId;
        this.content = content;
        this.title = title;
    }
}
