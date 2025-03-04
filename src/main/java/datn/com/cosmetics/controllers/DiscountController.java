package datn.com.cosmetics.controllers;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import datn.com.cosmetics.bean.request.DiscountRequest;
import datn.com.cosmetics.bean.response.ApiResponse;
import datn.com.cosmetics.bean.response.DiscountDTO;
import datn.com.cosmetics.entity.Discount;
import datn.com.cosmetics.services.IDiscountService;

@RestController
@RequestMapping("/api/discounts")
public class DiscountController {

    @Autowired
    private IDiscountService discountService;

    @PostMapping
    public ResponseEntity<ApiResponse<Discount>> createDiscount(@RequestBody DiscountDTO discountDTO) {
        discountService.validateDiscountDTO(discountDTO);
        discountService.checkDuplicateDiscountName(discountDTO.getName());
        discountService.checkDuplicateDiscountCode(discountDTO.getDiscountCode());
        Discount createdDiscount = discountService.createDiscount(discountDTO);
        return ResponseEntity.ok(ApiResponse.success(createdDiscount, "Discount created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Discount>> getDiscountById(@PathVariable Long id) {
        Discount discountDTO = discountService.getDiscountById(id);
        return ResponseEntity.ok(ApiResponse.success(discountDTO, "Discount retrieved successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Discount>> updateDiscount(@PathVariable Long id,
            @RequestBody DiscountDTO discountDTO) {
        Discount updatedDiscount = discountService.updateDiscount(id, discountDTO);
        return ResponseEntity.ok(ApiResponse.success(updatedDiscount, "Discount updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDiscount(@PathVariable Long id) {
        discountService.deleteDiscount(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Discount deleted successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Discount>>> getAllDiscounts(@RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Discount> discounts = discountService.getAllDiscounts(search, page, size);
        ApiResponse.Pagination pagination = new ApiResponse.Pagination(discounts.getNumber() + 1,
                discounts.getTotalPages(),
                discounts.getTotalElements());
        String message = "Discounts retrieved successfully";

        return ResponseEntity.ok(ApiResponse.success(discounts.getContent(), message, pagination));
    }

    @PostMapping("/apply")
    public ResponseEntity<ApiResponse<BigDecimal>> applyDiscount(
            @RequestHeader("Authorization") String token,
            @RequestBody DiscountRequest discountCode) {
                System.out.println("discountCode: " + discountCode);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.status(401).body(ApiResponse.error("Not logged in"));
        }

        String username = authentication.getName();
        System.out.println("username: " + username);
        BigDecimal appliedDiscount = discountService.applyDiscountToCart(discountCode.getCode(), username);
        return ResponseEntity.ok(ApiResponse.success(appliedDiscount, "Discount applied successfully"));
    }
}
