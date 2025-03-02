package datn.com.cosmetics.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import datn.com.cosmetics.entity.Order;
import datn.com.cosmetics.entity.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);

    Order findByIdAndUser(Long id, User user);

    List<Order> findByStatus(String status);

    List<Order> findByStatusAndUser(String status, User user);

    @Query("SELECT MONTH(o.orderDate) AS month, YEAR(o.orderDate) AS year, " +
            "SUM(o.totalPrice) AS totalRevenue, SUM(o.totalDiscountedPrice) AS totalDiscountedRevenue, COUNT(o.id) AS totalOrders "
            +
            "FROM Order o WHERE o.status = 'DELIVERED' " +
            "GROUP BY YEAR(o.orderDate), MONTH(o.orderDate) " +
            "ORDER BY year DESC, month DESC")
    List<Object[]> getMonthlyRevenue();

    @Query("SELECT MONTH(o.orderDate) AS month, YEAR(o.orderDate) AS year, " +
            "SUM(o.totalPrice) AS totalRevenue, SUM(o.totalDiscountedPrice) AS totalDiscountedRevenue, COUNT(o.id) AS totalOrders "
            +
            "FROM Order o WHERE o.status = 'DELIVERED' " +
            "AND YEAR(o.orderDate) = :year " +
            "GROUP BY YEAR(o.orderDate), MONTH(o.orderDate) " +
            "ORDER BY month DESC")
    List<Object[]> getMonthlyRevenueByYear(int year);
}
