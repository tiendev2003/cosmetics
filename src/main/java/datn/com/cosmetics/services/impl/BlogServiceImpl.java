package datn.com.cosmetics.services.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import datn.com.cosmetics.bean.request.BlogRequest;
import datn.com.cosmetics.entity.Blog;
import datn.com.cosmetics.entity.BlogCategory;
import datn.com.cosmetics.entity.Tag;
import datn.com.cosmetics.repository.BlogCategoryRepository;
import datn.com.cosmetics.repository.BlogRepository;
import datn.com.cosmetics.repository.TagRepository;
import datn.com.cosmetics.repository.UserRepository;
import datn.com.cosmetics.services.IBlogService;

@Service
public class BlogServiceImpl implements IBlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private BlogCategoryRepository blogCategoryRepository;

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Blog createBlog(BlogRequest request) {

        Blog blog = new Blog();
        blog.setTitle(request.getTitle());
        blog.setContent(request.getContent());
        blog.setImage(request.getImage());
        blog.setStatus(request.getStatus());
        BlogCategory category = blogCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        blog.setCategory(category);
        blog.setAuthor(userRepository.findByEmail(request.getAuthor()));
        Set<Tag> tags = new HashSet<>();
        for (Long tagId : request.getTagIds()) {
            Tag tag = tagRepository.findById(tagId)
                    .orElseThrow(() -> new RuntimeException("Tag not found"));
            tags.add(tag);
        }
        blog.setTags(tags);

        return blogRepository.save(blog);
    }

    @Override
    public Blog getBlogById(Long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));
    }

    @Override
    public Page<Blog> getAllBlogs(Pageable pageable, String title) {
        if (title != null && !title.isEmpty()) {
            return blogRepository.findByTitleContaining(pageable, title);
        }
        return blogRepository.findAll(pageable);
    }

    @Override
    public Blog updateBlog(Long id, BlogRequest request) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        blog.setTitle(request.getTitle());
        blog.setContent(request.getContent());
        blog.setImage(request.getImage());
        blog.setStatus(request.getStatus());

        BlogCategory category = blogCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        blog.setCategory(category);

        Set<Tag> tags = new HashSet<>();
        for (Long tagId : request.getTagIds()) {
            Tag tag = tagRepository.findById(tagId)
                    .orElseThrow(() -> new RuntimeException("Tag not found"));
            tags.add(tag);
        }
        blog.setTags(tags);

        return blogRepository.save(blog);
    }

    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }
}
