package datn.com.cosmetics.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne
    private Order order;

    @ManyToOne
    private Product product;

    private int quantity;

    private double price;

    private double discountedPrice;

    public OrderItem(Product product, int quantity, double price, double discountedPrice) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.discountedPrice = discountedPrice;
    }
}
