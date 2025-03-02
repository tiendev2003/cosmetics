package datn.com.cosmetics.bean.request;

import datn.com.cosmetics.entity.Product;
import datn.com.cosmetics.entity.User;
import lombok.Data;

@Data
public class ReviewRequest {
    private String review;
    private float star;
    private Product product;
    private User user;

}
