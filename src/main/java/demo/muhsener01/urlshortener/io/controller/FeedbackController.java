package demo.muhsener01.urlshortener.io.controller;

import demo.muhsener01.urlshortener.shared.dto.FeedbackResponse;
import demo.muhsener01.urlshortener.shared.dto.GetAllFeedbackResponse;
import demo.muhsener01.urlshortener.mapper.FeedbackMapper;
import demo.muhsener01.urlshortener.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final FeedbackMapper feedbackMapper;

    @PostMapping
    public FeedbackResponse sendFeedback(@RequestParam(name = "title", required = false) String title,
                                         @RequestParam(name = "content", required = true) String feedbackContent) {

        return feedbackService.save(title, feedbackContent);

    }

    @GetMapping("/{feedbackId}")
    public FeedbackResponse getFeedback(@PathVariable(name = "feedbackId", required = true) UUID feedbackId) {
        return feedbackMapper.toResponse(feedbackService.findById(feedbackId));

    }

    @GetMapping("")
    public GetAllFeedbackResponse getAllFeedback(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "limit", defaultValue = "10") int limit) {
        if (page < 0 || limit < 1) {
            page = 0;
            limit = 10;
        }


        return feedbackService.findAll(page, limit);
    }

}
