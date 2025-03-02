package datn.com.cosmetics;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import datn.com.cosmetics.entity.Address;
import datn.com.cosmetics.entity.Blog;
import datn.com.cosmetics.entity.BlogCategory;
import datn.com.cosmetics.entity.Brand;
import datn.com.cosmetics.entity.Category;
import datn.com.cosmetics.entity.Discount;
import datn.com.cosmetics.entity.Product;
import datn.com.cosmetics.entity.ProductImage;
import datn.com.cosmetics.entity.Tag;
import datn.com.cosmetics.entity.User;
import datn.com.cosmetics.entity.enums.DiscountType;
import datn.com.cosmetics.repository.AddressRepository;
import datn.com.cosmetics.repository.BlogCategoryRepository;
import datn.com.cosmetics.repository.BlogRepository;
import datn.com.cosmetics.repository.BrandRepository;
import datn.com.cosmetics.repository.CategoryRepository;
import datn.com.cosmetics.repository.DiscountRepository;
import datn.com.cosmetics.repository.OrderItemRepository;
import datn.com.cosmetics.repository.OrderRepository;
import datn.com.cosmetics.repository.ProductImageRepository;
import datn.com.cosmetics.repository.ProductRepository;
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
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductImageRepository productImageRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private DiscountRepository discountRepository;

    @Override
    public void run(String... args) {
        try {
            seedUsers();
            seedAddresses();
            seedTags();
            seedBlogCategories();
            seedBrands();
            seedCategories();
            seedBlogs();
            seedProducts();
            seedDiscounts();
            // seedOrders();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void seedUsers() {
        if (userRepository.count() == 0) {
            List<User> users = List.of(
                    new User("john_doe", "john@example.com", passwordEncoder.encode("123123123"),
                            "https://example.com/avatar1.jpg", false, "USER"),
                    new User("jane_smith", "jane@example.com", passwordEncoder.encode("123123123"),
                            "https://example.com/avatar2.jpg", false, "USER"),
                    new User("admin", "admin@example.com", passwordEncoder.encode("admin123"),
                            "https://example.com/avatar3.jpg", false, "ADMIN"),
                    new User("peter_parker", "peter@example.com", passwordEncoder.encode("123123123"),
                            "https://example.com/avatar4.jpg", false, "USER"),
                    new User("tony_stark", "tony@example.com", passwordEncoder.encode("123123123"),
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

    private void seedProducts() {
        if (productRepository.count() == 0) {
            Category skincare = categoryRepository.findById(1L).orElse(null);
            Brand loreal = brandRepository.findById(1L).orElse(null);

            if (skincare != null && loreal != null) {
                List<Product> products = List.of(
                        new Product(null, "L'Oreal Revitalift Cream", "Anti-aging face cream",
                                new BigDecimal("300000"), new BigDecimal("250000"), true, 50,
                                "Retinol, Hyaluronic Acid", "Apply daily", null, skincare, loreal, null,
                                "available", LocalDateTime.now(), LocalDateTime.now(), new BigDecimal("16.67")),
                        new Product(null, "Neutrogena Hydro Boost", "Water gel moisturizer",
                                new BigDecimal("240000"), new BigDecimal("190000"), true, 30,
                                "Hyaluronic Acid", "Use morning and night", null, skincare, loreal, null,
                                "available", LocalDateTime.now(), LocalDateTime.now(), new BigDecimal("20.83")),
                        new Product(null, "CeraVe Hydrating Cleanser", "Facial cleanser for normal to dry skin",
                                new BigDecimal("150000"), new BigDecimal("120000"), true, 20,
                                "Ceramides, Hyaluronic Acid", "Use twice daily", null, skincare, loreal, null,
                                "available", LocalDateTime.now(), LocalDateTime.now(), new BigDecimal("20.00")),
                        new Product(null, "The Ordinary Niacinamide 10%", "Brightening serum",
                                new BigDecimal("120000"), new BigDecimal("90000"), true, 40,
                                "Niacinamide, Zinc", "Apply before moisturizer", null, skincare, loreal, null,
                                "available", LocalDateTime.now(), LocalDateTime.now(), new BigDecimal("25.00")),
                        new Product(null, "Clinique Dramatically Different Moisturizer",
                                "Hydrates and strengthens skin barrier", new BigDecimal("270000"),
                                new BigDecimal("220000"), true, 25, "Glycerin, Hyaluronic Acid", "Use daily",
                                null, skincare, loreal, null, "available", LocalDateTime.now(),
                                LocalDateTime.now(), new BigDecimal("18.52")));

                productRepository.saveAll(products);

                // Add images for each product
                for (Product product : products) {
                    List<ProductImage> images = List.of(
                            new ProductImage(null,
                                    "https://example.com/images/" + product.getName().replace(" ", "_") + "_1.jpg",
                                    "public_id_1", "active", LocalDateTime.now(), LocalDateTime.now(), product),
                            new ProductImage(null,
                                    "https://example.com/images/" + product.getName().replace(" ", "_") + "_2.jpg",
                                    "public_id_2", "active", LocalDateTime.now(), LocalDateTime.now(), product));
                    productImageRepository.saveAll(images);
                }
            }
        }
    }

    private void seedAddresses() {
        if (addressRepository.count() == 0) {
            List<User> users = userRepository.findAll();

            if (users.isEmpty()) {
                System.out.println("Không có người dùng nào để tạo địa chỉ!");
                return;
            }

            System.out.println("Bắt đầu thêm địa chỉ cho người dùng...");

            for (User user : users) {
                Address address1 = new Address(null, "John", "Doe", "123 Main St", "Los Angeles", "CA", "90001",
                        "1234567890", user.getEmail(), true, LocalDateTime.now(), LocalDateTime.now(), user);
                Address address2 = new Address(null, "Jane", "Doe", "456 Elm St", "San Francisco", "CA", "94102",
                        "0987654321", user.getEmail(), false, LocalDateTime.now(), LocalDateTime.now(), user);

                addressRepository.saveAll(List.of(address1, address2));

                System.out.println("Đã thêm 2 địa chỉ cho user: " + user.getUsername() + " (ID: " + user.getId() + ")");
            }

            System.out.println("Seed địa chỉ hoàn tất!");
        } else {
            System.out.println("Dữ liệu địa chỉ đã tồn tại, bỏ qua seed!");
        }
    }

    private void seedOrders() {
        if (orderRepository.count() == 0) {

            System.out.println("Seed đơn hàng hoàn tất!");
        } else {
            System.out.println("Dữ liệu đơn hàng đã tồn tại, bỏ qua seed!");
        }
    }

    private void seedDiscounts() {
        if (discountRepository.count() == 0) {
            List<Discount> discounts = List.of(

                    new Discount("Giảm 10% đơn hàng từ 200K", "SALE10", DiscountType.PERCENTAGE, new BigDecimal("10"),
                            new BigDecimal("200000"), new BigDecimal("50000"), 100, 0, null,
                            LocalDateTime.now(), LocalDateTime.now().plusDays(30), true),

                    new Discount("Giảm 20% đơn hàng từ 500K", "SALE20", DiscountType.PERCENTAGE, new BigDecimal("20"),
                            new BigDecimal("500000"), new BigDecimal("100000"), 50, 0, null,
                            LocalDateTime.now(), LocalDateTime.now().plusDays(30), true),

                    new Discount("Giảm 50K đơn hàng từ 300K", "DISCOUNT50", DiscountType.FIXED,
                            new BigDecimal("50000"),
                            new BigDecimal("300000"), new BigDecimal("50000"), 200, 0, null,
                            LocalDateTime.now(), LocalDateTime.now().plusDays(60), true),

                    new Discount("Miễn phí vận chuyển", "FREESHIP", DiscountType.FIXED, new BigDecimal("25000"),
                            new BigDecimal("100000"), new BigDecimal("25000"), 500, 0, null,
                            LocalDateTime.now(), LocalDateTime.now().plusDays(15), true),

                    new Discount("Giảm 15% cho đơn hàng từ 400K", "SUMMER15", DiscountType.PERCENTAGE,
                            new BigDecimal("15"),
                            new BigDecimal("400000"), new BigDecimal("80000"), 150, 0, null,
                            LocalDateTime.now(), LocalDateTime.now().plusDays(45), true),

                    new Discount("Mừng năm mới! Giảm 30%", "NEWYEAR30", DiscountType.PERCENTAGE, new BigDecimal("30"),
                            new BigDecimal("600000"), new BigDecimal("120000"), 80, 0, null,
                            LocalDateTime.now(), LocalDateTime.now().plusDays(30), true),

                    new Discount("Giảm 50K cho đơn hàng từ 400K", "SPRING50", DiscountType.FIXED,
                            new BigDecimal("50000"),
                            new BigDecimal("400000"), new BigDecimal("50000"), 120, 0, null,
                            LocalDateTime.now(), LocalDateTime.now().plusDays(25), true),

                    new Discount("Giảm 25% đơn hàng từ 350K", "AUTUMN25", DiscountType.PERCENTAGE, new BigDecimal("25"),
                            new BigDecimal("350000"), new BigDecimal("90000"), 100, 0, null,
                            LocalDateTime.now(), LocalDateTime.now().plusDays(40), true),

                    new Discount("Black Friday - Giảm 50% cực sốc!", "BLACKFRIDAY", DiscountType.PERCENTAGE,
                            new BigDecimal("50"),
                            new BigDecimal("700000"), new BigDecimal("150000"), 50, 0, null,
                            LocalDateTime.now(), LocalDateTime.now().plusDays(10), true),

                    new Discount("Cyber Monday - Giảm ngay 75K", "CYBERMONDAY", DiscountType.FIXED,
                            new BigDecimal("75000"),
                            new BigDecimal("500000"), new BigDecimal("75000"), 60, 0, null,
                            LocalDateTime.now(), LocalDateTime.now().plusDays(20), true));

            discountRepository.saveAll(discounts);
        }
    }

}