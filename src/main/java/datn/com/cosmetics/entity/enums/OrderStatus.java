package datn.com.cosmetics.entity.enums;

public enum OrderStatus {
    PENDING, // Chờ xử lý
    CONFIRMED, // Đã xác nhận
    PROCESSING,
    SHIPPED, // Đang giao
    DELIVERED, // Đã giao
    CANCELLED, // Đã hủy
    PAID
}