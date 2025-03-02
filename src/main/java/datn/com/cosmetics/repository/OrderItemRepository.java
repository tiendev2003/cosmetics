package datn.com.cosmetics.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import datn.com.cosmetics.entity.Order;
import datn.com.cosmetics.entity.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // deleteByOrder
    void deleteByOrder(Order id);

    @Query("SELECT c.name AS categoryName, " +
            "SUM(oi.price * oi.quantity) AS totalRevenue, " +
            "SUM(oi.discountedPrice * oi.quantity) AS totalDiscountedRevenue, " +
            "COUNT(DISTINCT oi.order.id) AS totalOrders " +
            "FROM OrderItem oi " +
            "JOIN oi.product p " +
            "JOIN p.category c " +
            "WHERE oi.order.status = 'DELIVERED' " +
            "GROUP BY c.name " +
            "ORDER BY totalRevenue DESC")
    List<Object[]> getCategoryRevenue();

    // Tổng doanh thu theo danh mục, lọc theo năm
    @Query("SELECT c.name AS categoryName, " +
            "SUM(oi.price * oi.quantity) AS totalRevenue, " +
            "SUM(oi.discountedPrice * oi.quantity) AS totalDiscountedRevenue, " +
            "COUNT(DISTINCT oi.order.id) AS totalOrders " +
            "FROM OrderItem oi " +
            "JOIN oi.product p " +
            "JOIN p.category c " +
            "WHERE oi.order.status = 'DELIVERED' " +
            "AND YEAR(oi.order.orderDate) = :year " +
            "GROUP BY c.name " +
            "ORDER BY totalRevenue DESC")
    List<Object[]> getCategoryRevenueByYear(int year);

    // Tổng doanh thu theo danh mục, lọc theo tháng & năm
    @Query("SELECT c.name AS categoryName, " +
            "SUM(oi.price * oi.quantity) AS totalRevenue, " +
            "SUM(oi.discountedPrice * oi.quantity) AS totalDiscountedRevenue, " +
            "COUNT(DISTINCT oi.order.id) AS totalOrders " +
            "FROM OrderItem oi " +
            "JOIN oi.product p " +
            "JOIN p.category c " +
            "WHERE oi.order.status = 'DELIVERED' " +
            "AND MONTH(oi.order.orderDate) = :month " +
            "AND YEAR(oi.order.orderDate) = :year " +
            "GROUP BY c.name " +
            "ORDER BY totalRevenue DESC")
    List<Object[]> getCategoryRevenueByMonthAndYear(int month, int year);
}
