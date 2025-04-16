package demo.muhsener01.urlshortener.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GetAllFeedbackResponse {
    private List<FeedbackResponse> data;
    private long totalRecords;
    private int page;
    private int limit;
    private int totalPages;
}
