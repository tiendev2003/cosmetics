package datn.com.cosmetics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import datn.com.cosmetics.entity.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

}
