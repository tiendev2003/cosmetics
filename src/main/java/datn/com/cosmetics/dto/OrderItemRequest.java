package datn.com.cosmetics.dto;

import lombok.Data;

@Data
public class OrderItemRequest {
    private Long productId;
    private int quantity;
    private double price;
    private double discountedPrice;
}