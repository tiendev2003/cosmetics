package datn.com.cosmetics.services;

import datn.com.cosmetics.bean.request.RegisterRequest;
import datn.com.cosmetics.entity.User;
import datn.com.cosmetics.exceptions.UserNotFoundException;
import jakarta.mail.MessagingException;

public interface IUserService {
    User login(String username, String password);

    User register(RegisterRequest user);

    User getUserInfo(String email);

    boolean changeAvatar(String email, String avatarUrl);

    User getUserById(Long id);

    boolean changePassword(String email, String newPassword);

    boolean sendOtpEmail(String email) throws MessagingException, UserNotFoundException;

    boolean verifyOtp(String email, String otp);
}
