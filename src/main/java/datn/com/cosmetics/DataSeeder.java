package datn.com.cosmetics;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import datn.com.cosmetics.entity.Blog;
import datn.com.cosmetics.entity.BlogCategory;
import datn.com.cosmetics.entity.Brand;
import datn.com.cosmetics.entity.Category;
import datn.com.cosmetics.entity.Tag;
import datn.com.cosmetics.entity.User;
import datn.com.cosmetics.repository.BlogCategoryRepository;
import datn.com.cosmetics.repository.BlogRepository;
import datn.com.cosmetics.repository.BrandRepository;
import datn.com.cosmetics.repository.CategoryRepository;
import datn.com.cosmetics.repository.TagRepository;
import datn.com.cosmetics.repository.UserRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private BlogCategoryRepository blogCategoryRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedUsers();
        seedTags();
        seedBlogCategories();
        seedBrands();
        seedCategories();
        seedBlogs();
    }

    private void seedUsers() {
        if (userRepository.count() == 0) {
            List<User> users = List.of(
                    new User("john_doe", "john@example.com", passwordEncoder.encode("password123"),
                            "https://example.com/avatar1.jpg", false, "USER"),
                    new User("jane_smith", "jane@example.com", passwordEncoder.encode("password123"),
                            "https://example.com/avatar2.jpg", false, "USER"),
                    new User("admin", "admin@example.com", passwordEncoder.encode("admin123"),
                            "https://example.com/avatar3.jpg", false, "ADMIN"),
                    new User("peter_parker", "peter@example.com", passwordEncoder.encode("password123"),
                            "https://example.com/avatar4.jpg", false, "USER"),
                    new User("tony_stark", "tony@example.com", passwordEncoder.encode("password123"),
                            "https://example.com/avatar5.jpg", false, "USER"));
            userRepository.saveAll(users);
        }
    }

    private void seedTags() {
        if (tagRepository.count() == 0) {
            List<Tag> tags = List.of(
                    new Tag("Beauty"), new Tag("Skincare"), new Tag("Makeup"),
                    new Tag("Haircare"), new Tag("Lifestyle"));
            tagRepository.saveAll(tags);
        }
    }

    private void seedBlogCategories() {
        if (blogCategoryRepository.count() == 0) {
            List<BlogCategory> categories = List.of(
                    new BlogCategory("Skincare", "All about skincare"),
                    new BlogCategory("Makeup", "Latest makeup trends"),
                    new BlogCategory("Haircare", "Tips for healthy hair"),
                    new BlogCategory("Fashion", "Latest fashion trends"),
                    new BlogCategory("Lifestyle", "Healthy and balanced life"));
            blogCategoryRepository.saveAll(categories);
        }
    }

    private void seedBrands() {
        if (brandRepository.count() == 0) {
            List<Brand> brands = List.of(
                    new Brand("L'Oreal", "Luxury beauty brand", "https://example.com/loreal.jpg"),
                    new Brand("Maybelline", "Affordable makeup products", "https://example.com/maybelline.jpg"),
                    new Brand("Clinique", "Dermatologist-tested skincare", "https://example.com/clinique.jpg"),
                    new Brand("Dior", "High-end beauty and fashion", "https://example.com/dior.jpg"),
                    new Brand("MAC", "Professional makeup brand", "https://example.com/mac.jpg"));
            brandRepository.saveAll(brands);
        }
    }

    private void seedCategories() {
        if (categoryRepository.count() == 0) {
            List<Category> categories = List.of(
                    new Category("Skincare", "Products for healthy skin", "https://example.com/skincare.jpg"),
                    new Category("Makeup", "Cosmetics for beauty", "https://example.com/makeup.jpg"),
                    new Category("Haircare", "Shampoos and conditioners", "https://example.com/haircare.jpg"),
                    new Category("Perfume", "Luxury fragrances", "https://example.com/perfume.jpg"),
                    new Category("Accessories", "Beauty tools and accessories", "https://example.com/accessories.jpg"));
            categoryRepository.saveAll(categories);
        }
    }

    private void seedBlogs() {
        if (blogRepository.count() == 0) {
            BlogCategory category = blogCategoryRepository.findById(1L).orElse(null);
            User author = userRepository.findById(1L).orElse(null);
            List<Tag> tags = tagRepository.findAll();

            if (category != null && author != null && !tags.isEmpty()) {
                List<Blog> blogs = List.of(
                        new Blog("Top 10 Skincare Tips", "Best skincare practices", "https://example.com/blog1.jpg",
                                "published", category, author, Set.of(tags.get(0), tags.get(1))),
                        new Blog("Makeup Trends 2024", "Latest trends in makeup", "https://example.com/blog2.jpg",
                                "published", category, author, Set.of(tags.get(2))),
                        new Blog("How to Get Shiny Hair", "Haircare routines", "https://example.com/blog3.jpg",
                                "published", category, author, Set.of(tags.get(3))),
                        new Blog("Dressing Like a Celebrity", "Fashion trends", "https://example.com/blog4.jpg",
                                "published", category, author, Set.of(tags.get(4))),
                        new Blog("Healthy Lifestyle Tips", "How to stay healthy", "https://example.com/blog5.jpg",
                                "published", category, author, Set.of(tags.get(0), tags.get(4))));
                blogRepository.saveAll(blogs);
            }
        }
    }
}