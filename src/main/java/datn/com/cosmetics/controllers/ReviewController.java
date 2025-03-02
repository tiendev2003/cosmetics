package datn.com.cosmetics.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import datn.com.cosmetics.bean.request.ReviewRequest;
import datn.com.cosmetics.bean.response.ApiResponse;
import datn.com.cosmetics.entity.Review;
import datn.com.cosmetics.services.IReviewService;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private IReviewService reviewService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Review>> addReview(@RequestBody ReviewRequest reviewRequest) {
        Review review = reviewService.addReview(reviewRequest);
        ApiResponse<Review> response = ApiResponse.success(review, "Review added successfully");
        return ResponseEntity.ok(response);
    }

}
