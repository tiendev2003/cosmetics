package datn.com.cosmetics.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import datn.com.cosmetics.bean.request.ReviewRequest;
import datn.com.cosmetics.entity.Product;
import datn.com.cosmetics.entity.Review;
import datn.com.cosmetics.entity.User;
import datn.com.cosmetics.repository.OrderItemRepository;
import datn.com.cosmetics.repository.ProductRepository;
import datn.com.cosmetics.repository.ReviewRepository;
import datn.com.cosmetics.repository.UserRepository;
import datn.com.cosmetics.services.IReviewService;

@Service
public class ReviewServiceImpl implements IReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private UserRepository userRepository;
 
    @Override
    public Review addReview(ReviewRequest reviewRequest, String username) {
        // kiểm tra sản phẩm đã được mua chưa
        User user = userRepository.findByEmail(username);
        if (user == null) {
            return null;
        }
        if (orderItemRepository.checkProductIsBought(user.getId(), reviewRequest.getProductId()) == 0) {
            return null;
        }
        Product product = productRepository.findById(reviewRequest.getProductId()).get();
        if (product == null) {
            return null;
        }
        // check xem đã review chưa
        if (reviewRepository.findByProductIdAndUserId(product.getId(), user.getId()) != null) {
            return null;
        }
        Review review = new Review();
        review.setReview(reviewRequest.getReview());
        review.setStar(reviewRequest.getStar());
        review.setProduct(productRepository.findById(reviewRequest.getProductId()).get());
        review.setUser(user);
        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviewByProductId(Long productId) {
        Product product = productRepository.findById(productId).get();
        if (product == null) {
            return null;
        }
        return reviewRepository.findByProductId(product.getId());
    }

    

}
