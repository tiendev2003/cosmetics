package datn.com.cosmetics;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

import datn.com.cosmetics.entity.Address;
import datn.com.cosmetics.entity.Blog;
import datn.com.cosmetics.entity.BlogCategory;
import datn.com.cosmetics.entity.Brand;
import datn.com.cosmetics.entity.Category;
import datn.com.cosmetics.entity.Discount;
import datn.com.cosmetics.entity.Order;
import datn.com.cosmetics.entity.OrderItem;
import datn.com.cosmetics.entity.Product;
import datn.com.cosmetics.entity.Tag;
import datn.com.cosmetics.entity.User;
import datn.com.cosmetics.entity.enums.DiscountType;
import datn.com.cosmetics.entity.enums.OrderStatus;
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
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Component
public class DataSeeder implements CommandLineRunner {

        private final Faker faker = new Faker();

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
        @PersistenceContext
        private EntityManager entityManager;

        @Override
        @Transactional
        public void run(String... args) {
                try {
                        seedBlogCategories();
                        seedBrands();
                        seedCategories();
                        seedTags();
                        seedBlogs();
                        seedUsers();
                        seedAddresses();
                        seedProducts();
                        seedDiscounts();
                        seedOrders();
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        private void seedBlogCategories() {
                if (blogCategoryRepository.count() == 0) {
                        for (int i = 0; i < 1000; i++) {
                                entityManager.persist(new BlogCategory(faker.book().genre(), faker.lorem().sentence()));
                        }
                        entityManager.flush();
                        entityManager.clear();
                        System.out.println("✅ Seeded Blog Categories!");
                }
        }

        private void seedBrands() {
                if (brandRepository.count() == 0) {
                        for (int i = 0; i < 1000; i++) {
                                entityManager.persist(new Brand(faker.company().name(), faker.lorem().sentence(),
                                                faker.internet().image()));
                        }
                        entityManager.flush();
                        entityManager.clear();
                        System.out.println("✅ Seeded Brands!");
                }
        }

        private void seedCategories() {
                if (categoryRepository.count() == 0) {
                        for (int i = 0; i < 1000; i++) {
                                entityManager.persist(new Category(faker.commerce().department(),
                                                faker.lorem().sentence(), faker.internet().image()));
                        }
                        entityManager.flush();
                        entityManager.clear();
                        System.out.println("✅ Seeded Categories!");
                }
        }

        private void seedTags() {
                if (tagRepository.count() == 0) {
                        for (int i = 0; i < 1000; i++) {
                                entityManager.persist(new Tag(faker.commerce().productName()));
                        }
                        entityManager.flush();
                        entityManager.clear();
                        System.out.println("✅ Seeded Tags!");
                }
        }

        private void seedBlogs() {
                if (entityManager.createQuery("SELECT COUNT(b) FROM Blog b", Long.class).getSingleResult() == 0) {
                        List<BlogCategory> blogCategories = blogCategoryRepository.findAll();
                        List<User> users = userRepository.findAll();
                        List<Tag> tags = tagRepository.findAll();

                        if (blogCategories.isEmpty() || users.isEmpty() || tags.isEmpty()) {
                                System.out.println("⛔ Skipping blog seeding due to missing data.");
                                return;
                        }

                        for (int i = 0; i < 1000; i++) {
                                BlogCategory category = blogCategories
                                                .get(faker.number().numberBetween(0, blogCategories.size()));
                                User author = users.get(faker.number().numberBetween(0, users.size()));

                                Set<Tag> blogTags = new HashSet<>();
                                for (int j = 0; j < faker.number().numberBetween(1, 5); j++) {
                                        blogTags.add(tags.get(faker.number().numberBetween(0, tags.size())));
                                }

                                Blog blog = new Blog(
                                                faker.book().title(),
                                                faker.lorem().paragraph(),
                                                faker.internet().image(),
                                                "PUBLISHED",
                                                category,
                                                author,
                                                blogTags);

                                entityManager.persist(blog);

                                if (i % 50 == 0) { // Flush sau mỗi 50 bản ghi
                                        entityManager.flush();
                                        entityManager.clear();
                                }
                        }
                        entityManager.flush();
                        entityManager.clear();
                        System.out.println("✅ Seeded 1000 Blogs!");
                }
        }

        private void seedUsers() {
                if (userRepository.count() == 0) {
                        for (int i = 0; i < 50; i++) {
                                String username;
                                String email;

                                // Tạo username và email không trùng lặp
                                do {
                                        username = faker.name().username();
                                } while (userRepository.existsByUsername(username));

                                do {
                                        email = faker.internet().emailAddress();
                                } while (userRepository.existsByEmail(email));

                                User user = new User(
                                                username,
                                                email,
                                                passwordEncoder.encode("123123123"),
                                                faker.internet().avatar(),
                                                false,
                                                "USER");

                                entityManager.persist(user);
                        }
                        entityManager.flush();
                        entityManager.clear();
                        System.out.println("✅ Seeded Users!");
                }
        }

        private void seedProducts() {
                if (productRepository.count() == 0) {
                        List<Category> categories = categoryRepository.findAll();
                        List<Brand> brands = brandRepository.findAll();

                        if (categories.isEmpty() || brands.isEmpty()) {
                                System.out.println("⛔ Skipping product seeding due to missing data.");
                                return;
                        }

                        for (int i = 0; i < 1000; i++) {
                                Product product = new Product(
                                                null,

                                                faker.commerce().productName(), faker.lorem().sentence(),

                                                new BigDecimal(faker.number().numberBetween(10000, 5000000)),

                                                new BigDecimal(faker.number().numberBetween(5000, 4000000)),
                                                true,
                                                faker.number().numberBetween(10, 100),

                                                faker.lorem().sentence(),

                                                faker.lorem().sentence(),

                                                null,
                                                categories.get(faker.number().numberBetween(0, categories.size())),

                                                brands.get(faker.number().numberBetween(0, brands.size())),

                                                new ArrayList<>(),

                                                "ACTIVE",
                                                LocalDateTime.now(),
                                                LocalDateTime.now(),
                                                new BigDecimal("16.67"));
                                entityManager.persist(product);

                                if (i % 50 == 0) {
                                        entityManager.flush();
                                        entityManager.clear();
                                }
                        }
                        System.out.println("✅ Seeded 1000 Products!");
                }
        }

        private void seedAddresses() {
                if (addressRepository.count() == 0) {
                        List<User> users = userRepository.findAll();

                        if (users.isEmpty()) {
                                System.out.println("⛔ Skipping address seeding due to missing users.");
                                return;
                        }

                        for (User user : users) {
                                Address address = new Address(
                                                null, // ID phải để null để tránh xung đột
                                                faker.name().firstName(),
                                                faker.name().lastName(),
                                                faker.address().streetAddress(),
                                                faker.address().city(),
                                                faker.address().state(),
                                                faker.address().zipCode(),
                                                faker.phoneNumber().cellPhone(),
                                                user.getEmail(),
                                                true,
                                                LocalDateTime.now(),
                                                LocalDateTime.now(),
                                                user);

                                entityManager.merge(address); // Dùng merge thay vì persist
                        }
                        entityManager.flush();
                        entityManager.clear();
                        System.out.println("✅ Seeded Addresses!");
                }
        }

        private void seedDiscounts() {
                if (discountRepository.count() == 0) {
                        for (int i = 0; i < 10; i++) {
                                Discount discount = new Discount(
                                                faker.commerce().promotionCode(),
                                                faker.commerce().promotionCode(),
                                                DiscountType.PERCENTAGE,
                                                new BigDecimal(faker.number().numberBetween(5, 30)),
                                                new BigDecimal(100),
                                                new BigDecimal(500),
                                                faker.number().numberBetween(50, 500),
                                                0,
                                                null,
                                                LocalDateTime.now(),
                                                LocalDateTime.now().plusMonths(3),
                                                true);
                                entityManager.persist(discount);
                        }
                        entityManager.flush();
                        entityManager.clear();
                        System.out.println("✅ Seeded Discounts!");
                }
        }

        private void seedOrders() {
                if (orderRepository.count() == 0) {
                        List<User> users = userRepository.findAll();
                        List<Product> products = productRepository.findAll();
                        List<Address> addresses = addressRepository.findAll();

                        if (users.isEmpty() || products.isEmpty() || addresses.isEmpty()) {
                                System.out.println("⛔ Skipping order seeding due to missing data.");
                                return;
                        }

                        for (int i = 0; i < 1000; i++) {
                                User user = users.get(faker.number().numberBetween(0, users.size()));
                                Address address = addresses.get(faker.number().numberBetween(0, addresses.size()));
                                Order order = new Order();
                                order.setUser(user);
                                order.setShippingAddress(address);
                                order.setPaymentMethod("CASH");
                                order.setStatus(OrderStatus.DELIVERED);
                                order.setTotalAmount(new BigDecimal(faker.number().numberBetween(100000, 5000000)));
                                order.setDiscountAmount(BigDecimal.ZERO);
                                order.setFinalAmount(order.getTotalAmount());
                                order.setOrderDate(LocalDateTime.now());
                                entityManager.persist(order);

                                int orderItemCount = faker.number().numberBetween(1, 5);
                                for (int j = 0; j < orderItemCount; j++) {
                                        Product product = products
                                                        .get(faker.number().numberBetween(0, products.size()));
                                        OrderItem orderItem = new OrderItem(product, faker.number().numberBetween(1, 5),
                                                        product.getPrice());
                                        orderItem.setOrder(order);
                                        entityManager.persist(orderItem);
                                }

                                if (i % 50 == 0) {
                                        entityManager.flush();
                                        entityManager.clear();
                                }
                        }
                        System.out.println("✅ Seeded 500 Orders with Order Items!");
                }
        }

}