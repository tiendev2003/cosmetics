
package datn.com.cosmetics.bean.request;

import java.util.List;

import lombok.Data;

@Data
public class OrderRequest {
    String username;
    Long address;
    String paymentMethod;
    List<OrderItemRequest> orderItems;
    double totalPrice;

}