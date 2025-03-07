package datn.com.cosmetics.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import datn.com.cosmetics.entity.Discount;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
    Discount findByDiscountCode(String discountCode);

    Page<Discount> findByDiscountCodeContainingIgnoreCase(String search, Pageable pageable);

    Page<Discount> findByDiscountCodeContainingIgnoreCaseOrNameContainingIgnoreCase(String code, String name, Pageable pageable);

    boolean existsByDiscountCode(String discountCode);

    boolean existsByName(String discountName);
}