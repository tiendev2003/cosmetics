package datn.com.cosmetics.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import datn.com.cosmetics.entity.BlogCategory;

@Repository
public interface BlogCategoryRepository extends JpaRepository<BlogCategory, Long> {
    Page<BlogCategory> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
