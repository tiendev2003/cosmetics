package datn.com.cosmetics.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import datn.com.cosmetics.bean.request.ReviewRequest;
import datn.com.cosmetics.bean.response.ApiResponse;
import datn.com.cosmetics.entity.Review;
import datn.com.cosmetics.repository.ReviewRepository;
import datn.com.cosmetics.services.IReviewService;

@Service
public class ReviewServiceImpl implements IReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public Review addReview(ReviewRequest reviewRequest) {
        Review review = new Review();
        review.setReview(reviewRequest.getReview());
        review.setStar(reviewRequest.getStar());
        review.setProduct(reviewRequest.getProduct());
        review.setUser(reviewRequest.getUser());
        reviewRepository.save(review);
        return review;
    }
}
