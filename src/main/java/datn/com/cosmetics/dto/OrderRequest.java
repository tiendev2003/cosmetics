
package datn.com.cosmetics.dto;

import java.util.List;

import lombok.Data;

@Data
public class OrderRequest {
    String username;
    Long address;
    List<OrderItemRequest> orderItems;
    double totalPrice;

}