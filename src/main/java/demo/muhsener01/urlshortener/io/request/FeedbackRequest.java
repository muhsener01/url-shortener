package demo.muhsener01.urlshortener.io.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackRequest {

    private UUID userId;
    private String content;


}
