package datn.com.cosmetics.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import datn.com.cosmetics.entity.Blog;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    Page<Blog> findByTitleContaining(Pageable pageable, String title);
}
