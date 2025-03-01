package datn.com.cosmetics.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import datn.com.cosmetics.entity.Order;
import datn.com.cosmetics.entity.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);

    Order findByIdAndUser(Long id, User user);

    List<Order> findByStatus(String status);

    List<Order> findByStatusAndUser(String status, User user);

    
}
