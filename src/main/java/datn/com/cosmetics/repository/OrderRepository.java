package datn.com.cosmetics.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import datn.com.cosmetics.entity.Order;
import datn.com.cosmetics.entity.User;
import datn.com.cosmetics.entity.enums.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);

    Order findByIdAndUser(Long id, User user);
    Page<Order> findAll(Pageable pageable);
    List<Order> findByStatus(OrderStatus status);

    List<Order> findByStatusAndUser(OrderStatus status, User user);

    @Query("SELECT MONTH(o.orderDate) AS month, YEAR(o.orderDate) AS year, " +
            "SUM(o.totalAmount) AS totalRevenue, SUM(o.finalAmount) AS totalDiscountedRevenue, COUNT(o.id) AS totalOrders " +
            "FROM Order o WHERE o.status = 'DELIVERED' " +
            "GROUP BY YEAR(o.orderDate), MONTH(o.orderDate) " +
            "ORDER BY year DESC, month DESC")
    List<Object[]> getMonthlyRevenue();

    @Query("SELECT MONTH(o.orderDate) AS month, YEAR(o.orderDate) AS year, " +
            "SUM(o.totalAmount) AS totalRevenue, SUM(o.finalAmount) AS totalDiscountedRevenue, COUNT(o.id) AS totalOrders " +
            "FROM Order o WHERE o.status = 'DELIVERED' " +
            "AND YEAR(o.orderDate) = :year " +
            "GROUP BY YEAR(o.orderDate), MONTH(o.orderDate) " +
            "ORDER BY month DESC")
    List<Object[]> getMonthlyRevenueByYear(int year);
}
