package datn.com.cosmetics.services.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import datn.com.cosmetics.bean.response.DiscountDTO;
import datn.com.cosmetics.entity.Cart;
import datn.com.cosmetics.entity.CartItem;
import datn.com.cosmetics.entity.Discount;
import datn.com.cosmetics.entity.User;
import datn.com.cosmetics.entity.enums.DiscountType;
import datn.com.cosmetics.exceptions.DuplicateDiscountCodeException;
import datn.com.cosmetics.exceptions.DuplicateDiscountNameException;
import datn.com.cosmetics.exceptions.UserNotFoundException;
import datn.com.cosmetics.exceptions.ValidationException;
import datn.com.cosmetics.repository.DiscountRepository;
import datn.com.cosmetics.repository.OrderRepository;
import datn.com.cosmetics.services.IDiscountService;
import datn.com.cosmetics.services.IUserService;

@Service
public class DiscountServiceImpl implements IDiscountService {
    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartServiceImpl cartService;

    @Autowired
    private IUserService userService;

    @Override
    public BigDecimal applyDiscountToCart(String discountCode, String username) {
        try {
            User user = userService.getUserByEmail(username);
            if (user == null) {
                throw new UserNotFoundException("User not found");
            }
            if (discountCode == null || discountCode.isEmpty()) {
                throw new Exception("Discount code is empty");
            }
            Discount discount = discountRepository.findByDiscountCode(discountCode);
            if (discount == null) {
                throw new Exception("Discount code not found");
            }

            Cart cart = cartService.getCartUser(username);
            if (cart == null) {
                throw new Exception("Cart not found");
            }
            BigDecimal discountAmount = calculateDiscount(cart.getCartItems(), discount);

            return discountAmount;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public BigDecimal calculateDiscount(Set<CartItem> cartItems, Discount discount) {
        BigDecimal applicableAmount = calculateApplicableAmount(cartItems, discount.getApplicableProductId());
        BigDecimal discountAmount;
        if (discount.getDiscountType() == DiscountType.PERCENTAGE) {

            discountAmount = applicableAmount
                    .multiply(discount.getDiscountValue())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            if (discount.getMaxDiscountAmount() != null &&
                    discountAmount.compareTo(discount.getMaxDiscountAmount()) > 0) {
                discountAmount = discount.getMaxDiscountAmount();
            }
        } else { // FIXED
            discountAmount = discount.getDiscountValue();
            if (discountAmount.compareTo(applicableAmount) > 0) {
                discountAmount = applicableAmount;
            }
        }

        return discountAmount;
    }

    @Override
    public BigDecimal calculateApplicableAmount(Set<CartItem> items, Long applicableProductId) {
        if (applicableProductId == null) {
            return items.stream()
                    .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        return items.stream()
                .filter(item -> item.getProduct().getId().equals(applicableProductId))
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Discount createDiscount(DiscountDTO discountDTO) {
        Discount discount = new Discount();
        discount.setName(discountDTO.getName());
        discount.setDiscountCode(discountDTO.getDiscountCode());
        discount.setDiscountValue(discountDTO.getDiscountValue());
        discount.setDiscountType(discountDTO.getDiscountType());
        discount.setMaxDiscountAmount(discountDTO.getMaxDiscountAmount());
        discount.setApplicableProductId(discountDTO.getApplicableProductId());
        discountRepository.save(discount);
        return discount;
    }

    @Override
    public Discount getDiscountById(Long id) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Discount not found"));
        return discount;
    }

    @Override
    public Discount updateDiscount(Long id, DiscountDTO discountDTO) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Discount not found"));

        // Kiểm tra trùng lặp tên (chỉ khi tên thay đổi)
        if (!discount.getName().equals(discountDTO.getName()) &&
                discountRepository.existsByName(discountDTO.getName())) {
            throw new RuntimeException("Discount name already exists: " + discountDTO.getName());
        }

        // Kiểm tra trùng lặp mã giảm giá (chỉ khi mã thay đổi)
        if (!discount.getDiscountCode().equals(discountDTO.getDiscountCode()) &&
                discountRepository.existsByDiscountCode(discountDTO.getDiscountCode())) {
            throw new RuntimeException("Discount code already exists: " + discountDTO.getDiscountCode());
        }

        // Cập nhật thông tin
        discount.setName(discountDTO.getName());
        discount.setDiscountCode(discountDTO.getDiscountCode());
        discount.setDiscountValue(discountDTO.getDiscountValue());
        discount.setDiscountType(discountDTO.getDiscountType());
        discount.setMaxDiscountAmount(discountDTO.getMaxDiscountAmount());
        discount.setApplicableProductId(discountDTO.getApplicableProductId());

        return discountRepository.save(discount);
    }

    @Override
    public void deleteDiscount(Long id) {
        discountRepository.deleteById(id);
    }

    @Override
    public Page<Discount> getAllDiscounts(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (search == null || search.isEmpty()) {
            return discountRepository.findAll(pageable);
        } else {
            return discountRepository.findByDiscountCodeContainingIgnoreCase(search, pageable);
        }
    }

    @Override
    public void validateDiscountDTO(DiscountDTO discountDTO) {
        if (discountDTO.getName() == null || discountDTO.getName().isEmpty()) {
            throw new ValidationException("Discount name is required");
        }
        if (discountDTO.getDiscountCode() == null || discountDTO.getDiscountCode().isEmpty()) {
            throw new ValidationException("Discount code is required");
        }
        if (discountDTO.getDiscountValue() == null || discountDTO.getDiscountValue().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Discount value must be greater than zero");
        }
    }

    @Override
    public void checkDuplicateDiscountName(String name) {
        if (discountRepository.existsByName(name)) {
            throw new DuplicateDiscountNameException("Discount name already exists");
        }
    }

    @Override
    public void checkDuplicateDiscountCode(String code) {
        if (discountRepository.existsByDiscountCode(code)) {
            throw new DuplicateDiscountCodeException("Discount code already exists");
        }
    }
}
