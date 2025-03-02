package datn.com.cosmetics.services.impl;

import java.security.SecureRandom;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import datn.com.cosmetics.bean.request.RegisterRequest;
import datn.com.cosmetics.bean.request.UserRequest;
import datn.com.cosmetics.entity.Cart;
import datn.com.cosmetics.entity.User;
import datn.com.cosmetics.exceptions.UserNotFoundException;
import datn.com.cosmetics.repository.CartRepository;
import datn.com.cosmetics.repository.UserRepository;
import datn.com.cosmetics.services.EmailService;
import datn.com.cosmetics.services.IUserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private EmailService emailService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public User login(String username, String password) {
        User user = userRepository.findByEmail(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    @Override
    public User register(RegisterRequest user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return null;
        }
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(user.getPassword());
        newUser.setEmail(user.getEmail());

        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(newUser);
        Cart cart = new Cart();
        cart.setUser(savedUser);
        cartRepository.save(cart);
        return savedUser;
    }

    @Override
    public User getUserInfo(String email) {
        User user = userRepository.findByEmail(email);
        return user;
    }

    @Override
    public boolean changeAvatar(String email, String avatarUrl) {

        User user = userRepository.findByEmail(email);
        System.out.println(avatarUrl);
        if (user != null) {
            user.setAvatar(avatarUrl);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public boolean sendOtpEmail(String email) throws MessagingException, UserNotFoundException {
        User userOpt = userRepository.findByEmail(email);
        if (userOpt == null) {
            throw new UserNotFoundException("User not found with email: " + email);
        }
        // Generate OTP and send email using EmailService
        String otp = generateOtp();
        Context context = new Context();
        context.setVariable("otp", otp);
        context.setVariable("name", userOpt.getUsername()); // Assuming username is the name

        emailService.sendEmail(email, "OTP Verification", "otp-email-template", context);
        User user = userOpt;
        user.setOtp(otp);
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean verifyOtp(String email, String otp) {
        User userOpt = userRepository.findByEmail(email);
        if (userOpt != null && userOpt.getOtp().equals(otp)) {
            userOpt.setOtp(null);
            userRepository.save(userOpt);
            return true;
        }
        return false;
    }

    @Override
    public boolean changePassword(String email, String oldPassword,String newPassword ) {
        User userOpt = userRepository.findByEmail(email);
        if (userOpt != null && passwordEncoder.matches(oldPassword, userOpt.getPassword())) {
            userOpt.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(userOpt);
            return true;
        }
        return false;
    }

    @Override
    public User getUserByEmail(String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User not found with email: " + email);
        }
        return user;
    }

    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public boolean deleteUserById(Long id) {
        userRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean blockUserById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setLocked(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public User updateUser(UserRequest user, String email) {
        User userOpt = userRepository.findByEmail(email);
        if (userOpt != null) {
            userOpt.setUsername(user.getUsername());
            userOpt.setBio(user.getBio());
            userOpt.setPhone(user.getPhone());
            User newUser = userRepository.save(userOpt);
            return newUser;
        }
        return null;
    }
}
