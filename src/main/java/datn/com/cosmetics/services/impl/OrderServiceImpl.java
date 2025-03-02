package datn.com.cosmetics.services.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import datn.com.cosmetics.bean.request.OrderRequest;
import datn.com.cosmetics.entity.Address;
import datn.com.cosmetics.entity.Order;
import datn.com.cosmetics.entity.OrderItem;
import datn.com.cosmetics.entity.User;
import datn.com.cosmetics.repository.AddressRepository;
import datn.com.cosmetics.repository.OrderItemRepository;
import datn.com.cosmetics.repository.OrderRepository;
import datn.com.cosmetics.repository.ProductRepository;
import datn.com.cosmetics.repository.UserRepository;
import datn.com.cosmetics.services.IOrderService;

@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Override
    @Transactional
    public Order createOrder(OrderRequest orderRequest) {
        // Validate input
        if (orderRequest.getTotalPrice() < 0 || orderRequest.getOrderItems().isEmpty()) {
            throw new IllegalArgumentException("Invalid order request");
        }

        User user = userRepository.findByEmail(orderRequest.getUsername());
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        List<Address> addresses = addressRepository.findByUser(user);
        if (addresses.isEmpty()) {
            throw new IllegalArgumentException("User has no address");
        }
        // tìm id address trong list address của user
        Address address = addresses.stream()
                .filter(a -> a.getId().equals(orderRequest.getAddress()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Address not found"));
        if (address.getUser().getId() != user.getId()) {
            return null;
        }

        if (!isValidPaymentMethod(orderRequest.getPaymentMethod())) {
            throw new IllegalArgumentException("Invalid payment method: " + orderRequest.getPaymentMethod());
        }

        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(address);
        order.setTotalPrice(orderRequest.getTotalPrice());
        order.setStatus("PENDING");
        order.setPaymentMethod(orderRequest.getPaymentMethod());

        Order newOrder = orderRepository.save(order);

        // Lưu OrderItem
        orderRequest.getOrderItems().forEach(itemRequest -> {
            if (itemRequest.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be positive");
            }
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(newOrder);
            orderItem.setProduct(productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found")));
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(itemRequest.getPrice());
            orderItem.setDiscountedPrice(itemRequest.getDiscountedPrice());
            orderItemRepository.save(orderItem);
        });

        return newOrder;
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> null);
    }

    @Override
    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Order changeStatusOrder(Long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> null);
        System.out.println("status: " + status);
        if (!isValidStatus(status)) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
        order.setStatus(status);
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> null);
        orderItemRepository.deleteByOrder(order); // Xóa OrderItem trước
        orderRepository.delete(order);
    }

    @Override
    public List<Order> getOrdersByUser(String username) {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            return Collections.emptyList();
        }
        return orderRepository.findByUser(user);
    }

    private boolean isValidStatus(String status) {
        return List.of("PENDING", "CONFIRMED", "DELIVERED", "CANCELLED", "PAID", "SHIPPED").contains(status);
    }

    private boolean isValidPaymentMethod(String paymentMethod) {
        return List.of("COD", "CREDIT_CARD", "MOMO").contains(paymentMethod);
    }
}