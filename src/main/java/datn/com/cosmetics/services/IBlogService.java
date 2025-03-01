package datn.com.cosmetics.services;

import datn.com.cosmetics.bean.request.BlogRequest;
import datn.com.cosmetics.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IBlogService {
    Blog createBlog(BlogRequest request);
    Blog getBlogById(Long id);
    Page<Blog> getAllBlogs(Pageable pageable, String title);
    Blog updateBlog(Long id, BlogRequest request);
    void deleteBlog(Long id);
}
