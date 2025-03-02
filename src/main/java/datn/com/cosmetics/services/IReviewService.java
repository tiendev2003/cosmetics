package datn.com.cosmetics.services;

import datn.com.cosmetics.bean.request.ReviewRequest;
import datn.com.cosmetics.entity.Review;

public interface IReviewService {
    Review addReview(ReviewRequest reviewRequest);
}
