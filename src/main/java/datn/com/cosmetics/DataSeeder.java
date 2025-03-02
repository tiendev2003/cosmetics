package datn.com.cosmetics;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import datn.com.cosmetics.entity.Address;
import datn.com.cosmetics.entity.Blog;
import datn.com.cosmetics.entity.BlogCategory;
import datn.com.cosmetics.entity.Brand;
import datn.com.cosmetics.entity.Category;
import datn.com.cosmetics.entity.Order;
import datn.com.cosmetics.entity.OrderItem;
import datn.com.cosmetics.entity.Product;
import datn.com.cosmetics.entity.ProductImage;
import datn.com.cosmetics.entity.Tag;
import datn.com.cosmetics.entity.User;
import datn.com.cosmetics.entity.enums.OrderStatus;
import datn.com.cosmetics.repository.AddressRepository;
import datn.com.cosmetics.repository.BlogCategoryRepository;
import datn.com.cosmetics.repository.BlogRepository;
import datn.com.cosmetics.repository.BrandRepository;
import datn.com.cosmetics.repository.CategoryRepository;
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
                        new Product(null, "L'Oreal Revitalift Cream", "Anti-aging face cream", 300000, 250000, 50,
                                "Retinol, Hyaluronic Acid", "Apply daily", null, skincare, loreal, null, "available",
                                LocalDateTime.now(), LocalDateTime.now()),
                        new Product(null, "Neutrogena Hydro Boost", "Water gel moisturizer", 240000, 190000, 30,
                                "Hyaluronic Acid", "Use morning and night", null, skincare, loreal, null, "available",
                                LocalDateTime.now(), LocalDateTime.now()),
                        new Product(null, "CeraVe Hydrating Cleanser", "Facial cleanser for normal to dry skin", 150000,
                                120000, 20, "Ceramides, Hyaluronic Acid", "Use twice daily", null, skincare, loreal,
                                null, "available", LocalDateTime.now(), LocalDateTime.now()),
                        new Product(null, "The Ordinary Niacinamide 10%", "Brightening serum", 120000, 90000, 40,
                                "Niacinamide, Zinc", "Apply before moisturizer", null, skincare, loreal, null,
                                "available", LocalDateTime.now(), LocalDateTime.now()),
                        new Product(null, "Clinique Dramatically Different Moisturizer",
                                "Hydrates and strengthens skin barrier", 270000, 220000, 25,
                                "Glycerin, Hyaluronic Acid",
                                "Use daily", null, skincare, loreal, null, "available", LocalDateTime.now(),
                                LocalDateTime.now()));

                productRepository.saveAll(products);

                // Thêm ảnh cho từng sản phẩm
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
            List<User> users = userRepository.findAll();
            List<Product> products = productRepository.findAll();
            Random random = new Random();

            if (users.isEmpty() || products.isEmpty()) {
                System.out.println("Không có đủ dữ liệu để tạo đơn hàng!");
                return;
            }

            System.out.println("Bắt đầu seed đơn hàng...");

            for (int i = 0; i < 15; i++) { // Seed 15 đơn hàng
                User randomUser = users.get(random.nextInt(users.size()));

                List<Address> userAddresses = addressRepository.findByUser(randomUser);
                if (userAddresses.isEmpty()) {
                    System.out.println("Không tìm thấy địa chỉ cho user: " + randomUser.getUsername());
                    continue;
                }

                Address shippingAddress = userAddresses.get(random.nextInt(userAddresses.size()));

                List<OrderItem> orderItems = products.stream()
                        .limit(random.nextInt(3) + 1) // Mỗi đơn hàng có từ 1-3 sản phẩm
                        .map(product -> new OrderItem(product, random.nextInt(5) + 1, product.getPrice(),
                                product.getSalePrice()))
                        .collect(Collectors.toList());

                double totalPrice = orderItems.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
                double totalDiscountedPrice = orderItems.stream()
                        .mapToDouble(item -> item.getDiscountedPrice() * item.getQuantity()).sum();
                double discount = totalPrice - totalDiscountedPrice;

                Order order = new Order();
                 order.setUser(randomUser);
                order.setOrderDate(LocalDateTime.now().minusDays(random.nextInt(30)));
                order.setDeliveryDate(order.getOrderDate().plusDays(random.nextInt(7) + 1));
                order.setShippingAddress(shippingAddress);
                order.setPaymentMethod(random.nextBoolean() ? "Credit Card" : "Cash on Delivery");
                order.setStatus(random.nextBoolean() ? OrderStatus.DELIVERED : OrderStatus.PENDING);
                order.setTotalPrice(totalPrice);
                order.setTotalDiscountedPrice(totalDiscountedPrice);
                order.setDiscount(discount);

                System.out.println("Tạo đơn hàng: " + order.getOrderId() + " cho user: " + randomUser.getUsername());

                Order savedOrder = orderRepository.save(order);

                for (OrderItem item : orderItems) {
                    item.setOrder(savedOrder);
                }
                orderItemRepository.saveAll(orderItems);
            }

            System.out.println("Seed đơn hàng hoàn tất!");
        } else {
            System.out.println("Dữ liệu đơn hàng đã tồn tại, bỏ qua seed!");
        }
    }

}