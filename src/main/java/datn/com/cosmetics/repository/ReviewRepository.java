package datn.com.cosmetics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import datn.com.cosmetics.entity.Review;
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
}
