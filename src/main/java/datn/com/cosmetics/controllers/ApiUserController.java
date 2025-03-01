package datn.com.cosmetics.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import datn.com.cosmetics.bean.request.LoginRequest;
import datn.com.cosmetics.bean.request.RegisterRequest;
import datn.com.cosmetics.bean.response.ApiResponse;
import datn.com.cosmetics.config.auth.JWTGenerator;
import datn.com.cosmetics.entity.User;
import datn.com.cosmetics.exceptions.UserNotFoundException;
import datn.com.cosmetics.services.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User API", description = "API for user operations")
public class ApiUserController {

    @Autowired
    private IUserService userService;
    @Autowired
    private JWTGenerator jwtGenerator;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Operation(summary = "Register a new user", description = "Register a new user with email and password")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> register(@RequestBody RegisterRequest request) {
        try {
            User user = userService.register(request);
            if (user == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Email already exists"));
            }
            return ResponseEntity.ok(ApiResponse.success(user, "User registered successfully"));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));

        }
    }

    @Operation(summary = "Login user", description = "Authenticate user and return JWT token")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginRequest request) {
        try {
            User user = userService.login(request.getEmail(), request.getPassword());
            if (user == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Invalid email or password"));
            }
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            return ResponseEntity
                    .ok(ApiResponse.success(jwtGenerator.generateToken(authentication), "Login successfully"));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));

        }
    }

    @Operation(summary = "Get user profile", description = "Get the profile of the logged-in user")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<User>> profile(
            @RequestHeader(name = "Authorization", required = false) String jwt) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()
                    || "anonymousUser".equals(authentication.getPrincipal())) {
                return ResponseEntity.status(401).body(ApiResponse.error("Not logged in"));
            }

            String username = authentication.getName();

            User user = userService.getUserInfo(username);
            if (user == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("User not found"));
            }
            return ResponseEntity.ok(ApiResponse.success(user, "User info"));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));

        }
    }

    @Operation(summary = "Update user information", description = "Update the avatar of the logged-in user")
    @PutMapping("/update")
    public ResponseEntity<ApiResponse<String>> updateUserInfo(
            @RequestHeader(name = "Authorization") String jwt,
            @RequestBody String avatar) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.status(401).body(ApiResponse.error("Not logged in"));
        }

        String username = authentication.getName();
        boolean updated = userService.changeAvatar(username, avatar);
        if (updated) {
            return ResponseEntity.ok(ApiResponse.success("User information updated successfully", ""));
        }
        return ResponseEntity.badRequest().body(ApiResponse.error("Failed to update user information"));
    }

    @Operation(summary = "Send OTP email", description = "Send an OTP to the user's email for verification")
    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse<String>> sendOtpEmail(@RequestParam String email) {
        try {
            boolean sent = userService.sendOtpEmail(email);
            if (sent) {
                return ResponseEntity.ok(ApiResponse.success("", "OTP sent successfully"));
            }
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to send OTP"));
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body(ApiResponse.error("Failed to send email: " + e.getMessage()));
        }
    }

    @Operation(summary = "Change user password", description = "Change the password of the user")
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(@RequestParam String email,
            @RequestParam String newPassword) {
        boolean changed = userService.changePassword(email, newPassword);
        if (changed) {
            return ResponseEntity.ok(ApiResponse.success("Password changed successfully", ""));
        }
        return ResponseEntity.badRequest().body(ApiResponse.error("Failed to change password"));
    }

    @Operation(summary = "Verify OTP", description = "Verify the OTP sent to the user's email")
    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<String>> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        boolean verified = userService.verifyOtp(email, otp);
        if (verified) {
            return ResponseEntity.ok(ApiResponse.success("OTP verified successfully", ""));
        }
        return ResponseEntity.badRequest().body(ApiResponse.error("Failed to verify OTP"));
    }
}
