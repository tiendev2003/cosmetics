package datn.com.cosmetics.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import datn.com.cosmetics.entity.Brand;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    Page<Brand> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Brand findByName(String name);

    boolean existsByName(String name);

    @Query("SELECT b.id AS brandId, b.name AS brandName, " +
            "c.id AS categoryId, c.name AS categoryName, " +
            "COUNT(p.id) AS productCount " +
            "FROM Product p " +
            "JOIN p.brand b " +
            "JOIN p.category c " +
            "GROUP BY b.id, b.name, c.id, c.name " +
            "ORDER BY COUNT(p.id) DESC " +
            "LIMIT 5")
    List<Object[]> getTop5BrandsWithCategoriesAndProductCount();
}
