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
import datn.com.cosmetics.repository.CartRepository;
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
    private CartRepository cartRepository;

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
            System.out.println("Discount code: " + discountCode);
            Discount discount = discountRepository.findByDiscountCode(discountCode);
            if (discount == null) {
                throw new Exception("Discount code not found");
            }

            Cart cart = cartService.getCartUser(username);
            if (cart == null) {
                throw new Exception("Cart not found");
            }
            cart.setDiscountCode(discountCode);
            cartRepository.save(cart);
            BigDecimal discountAmount = calculateDiscount(cart.getCartItems(), discount);

            return discountAmount;
        } catch (UserNotFoundException e) {
            throw new RuntimeException("User not found: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error applying discount: " + e.getMessage());
        }
    }

    @Override
    public BigDecimal calculateDiscount(Set<CartItem> cartItems, Discount discount) {
        try {
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
        } catch (Exception e) {
            throw new RuntimeException("Error calculating discount: " + e.getMessage());
        }
    }

    @Override
    public BigDecimal calculateApplicableAmount(Set<CartItem> items, Long applicableProductId) {
        try {
            return items.stream()
                    .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        } catch (Exception e) {
            throw new RuntimeException("Error calculating applicable amount: " + e.getMessage());
        }
    }

    @Override
    public Discount createDiscount(DiscountDTO discountDTO) {
        try {
            // Validate discount DTO
            validateDiscountDTO(discountDTO);

            // Check for duplicate discount name
            checkDuplicateDiscountName(discountDTO.getName());

            // Check for duplicate discount code
            checkDuplicateDiscountCode(discountDTO.getDiscountCode());

            Discount discount = new Discount();
            discount.setName(discountDTO.getName());
            discount.setMaxUsage(discountDTO.getMaxUsage());
            discount.setMinOrderValue(discountDTO.getMinOrderValue());
            discount.setActive(discountDTO.isActive());
            discount.setDiscountCode(discountDTO.getDiscountCode());
            discount.setDiscountValue(discountDTO.getDiscountValue());
            discount.setDiscountType(discountDTO.getDiscountType());
            discount.setMaxDiscountAmount(discountDTO.getMaxDiscountAmount());
            discount.setApplicableProductId(discountDTO.getApplicableProductId());
            discount.setStartDate(discountDTO.getStartDate());
            discount.setEndDate(discountDTO.getEndDate());
            discountRepository.save(discount);
            return discount;
        } catch (ValidationException e) {
            throw new RuntimeException("Validation error: " + e.getMessage());
        } catch (DuplicateDiscountNameException e) {
            throw new RuntimeException("Duplicate name error: " + e.getMessage());
        } catch (DuplicateDiscountCodeException e) {
            throw new RuntimeException("Duplicate code error: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error creating discount: " + e.getMessage());
        }
    }

    @Override
    public Discount getDiscountById(Long id) {
        try {
            Discount discount = discountRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Discount not found"));
            return discount;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving discount: " + e.getMessage());
        }
    }

    @Override
    public Discount updateDiscount(Long id, DiscountDTO discountDTO) {
        try {
            Discount discount = discountRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Discount not found"));

            // Validate discount DTO
            validateDiscountDTO(discountDTO);

            // Check for duplicate discount name (only if name is changed)
            if (!discount.getName().equals(discountDTO.getName())) {
                checkDuplicateDiscountName(discountDTO.getName());
            }

            // Check for duplicate discount code (only if code is changed)
            if (!discount.getDiscountCode().equals(discountDTO.getDiscountCode())) {
                checkDuplicateDiscountCode(discountDTO.getDiscountCode());
            }

            // Update discount information
            discount.setName(discountDTO.getName());
            discount.setMaxUsage(discountDTO.getMaxUsage());
            discount.setMinOrderValue(discountDTO.getMinOrderValue());
            discount.setActive(discountDTO.isActive());
            discount.setDiscountCode(discountDTO.getDiscountCode());
            discount.setDiscountValue(discountDTO.getDiscountValue());
            discount.setDiscountType(discountDTO.getDiscountType());
            discount.setMaxDiscountAmount(discountDTO.getMaxDiscountAmount());
            discount.setApplicableProductId(discountDTO.getApplicableProductId());
            discount.setStartDate(discountDTO.getStartDate());
            discount.setEndDate(discountDTO.getEndDate());

            return discountRepository.save(discount);
        } catch (ValidationException e) {
            throw new RuntimeException("Validation error: " + e.getMessage());
        } catch (DuplicateDiscountNameException e) {
            throw new RuntimeException("Duplicate name error: " + e.getMessage());
        } catch (DuplicateDiscountCodeException e) {
            throw new RuntimeException("Duplicate code error: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error updating discount: " + e.getMessage());
        }
    }

    @Override
    public void deleteDiscount(Long id) {
        discountRepository.deleteById(id);

    }

    @Override
    public Page<Discount> getAllDiscounts(String search, boolean isActive, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            if (search == null || search.isEmpty()) {
                return discountRepository.findAll(pageable);
            } else {
                return discountRepository.findByDiscountCodeContainingIgnoreCase(search, isActive, pageable);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving discounts: " + e.getMessage());
        }
    }

    @Override
    public Page<Discount> searchDiscounts(String code, String name, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return discountRepository.findByDiscountCodeContainingIgnoreCaseOrNameContainingIgnoreCase(code, name,
                    pageable);
        } catch (Exception e) {
            throw new RuntimeException("Error searching discounts: " + e.getMessage());
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
