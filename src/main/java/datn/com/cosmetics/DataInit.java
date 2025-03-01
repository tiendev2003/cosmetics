package datn.com.cosmetics;

import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInit {
    // @Bean
    // CommandLineRunner init(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    //     return args -> {
    //         User admin = new User();
    //         admin.setUsername("admin");
    //         admin.setPassword(passwordEncoder.encode("admin123"));
    //         admin.setLocked(false);
    //         admin.setEmail("admin@gmail.com");
    //         admin.setRole("ROLE_ADMIN");
    //         userRepository.save(admin);

    //         User user = new User();
    //         user.setUsername("user");
    //         user.setPassword(passwordEncoder.encode("user123"));
    //         user.setRole("ROLE_USER");
    //         userRepository.save(user);
    //     };
    // }

}
