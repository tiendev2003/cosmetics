package datn.com.cosmetics.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import datn.com.cosmetics.bean.request.CartRequest;
import datn.com.cosmetics.entity.Cart;
import datn.com.cosmetics.entity.CartItem;
import datn.com.cosmetics.entity.Product;
import datn.com.cosmetics.entity.User;
import datn.com.cosmetics.repository.CartItemRepository;
import datn.com.cosmetics.repository.CartRepository;
import datn.com.cosmetics.repository.ProductRepository;
import datn.com.cosmetics.repository.UserRepository;
import datn.com.cosmetics.services.ICartService;

@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Cart addToCart(CartRequest cartRequest, String username) {
        User user = userRepository.findByEmail(username);
        Product product = productRepository.findById(cartRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cart = cartRepository.findByUser(user).orElse(new Cart());
        cart.setUser(user);

        Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + cartRequest.getQuantity());
            cartItemRepository.save(cartItem);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(cartRequest.getQuantity());
            cart.getCartItems().add(cartItem);
            cartItemRepository.save(cartItem);
        }

        cart.setTotal(cart.getCartItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity()).sum());
        return cartRepository.save(cart);
    }

    @Override
    public Cart updateQuantity(Long cartItemId, int quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);

        Cart cart = cartItem.getCart();
        cart.setTotal(cart.getCartItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity()).sum());
        return cartRepository.save(cart);
    }

    @Override
    public void clearCart(String username) {
        User user = userRepository.findByEmail(username);
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Cart not found"));
        cartItemRepository.deleteAll(cart.getCartItems());
        cart.getCartItems().clear();
        cart.setTotal(0);
        cartRepository.save(cart);
    }

    @Override
    public Cart getCartUser(String username) {
        User user = userRepository.findByEmail(username);
        return cartRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Cart not found"));
    }

}
