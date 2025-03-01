package datn.com.cosmetics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import datn.com.cosmetics.entity.Order;
import datn.com.cosmetics.entity.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // deleteByOrder
    void deleteByOrder(Order id);
}
