package datn.com.cosmetics.bean.request;

import java.util.List;

import lombok.Data;

@Data
public class ProductRequest {
    private String name;
    private String description;
    private double price;
    private double salePrice;
    private int stock;
    private String ingredients;
    private String productUsage;
    private Long categoryId;
    private Long brandId;
    private String status;
    private List<String> images;
}
