package datn.com.cosmetics.dto;

import java.math.BigDecimal;

import datn.com.cosmetics.entity.enums.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscountDTO {
    private String name;
    private String discountCode;
    private BigDecimal discountValue;
    private DiscountType discountType;
    private BigDecimal maxDiscountAmount;
    private Long applicableProductId;

}
