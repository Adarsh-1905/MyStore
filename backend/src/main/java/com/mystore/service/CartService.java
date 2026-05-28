package com.mystore.service;

import com.mystore.model.OrderItem;
import com.mystore.model.OrderStatus;
import com.mystore.model.Product;
import com.mystore.model.StoreOrder;
import com.mystore.repository.ProductRepository;
import com.mystore.repository.StoreOrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final ProductRepository productRepository;
    private final StoreOrderRepository storeOrderRepository;

    public CartService(ProductRepository productRepository, StoreOrderRepository storeOrderRepository) {
        this.productRepository = productRepository;
        this.storeOrderRepository = storeOrderRepository;
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Optional<StoreOrder> getOpenCart() {
        return storeOrderRepository.findFirstByStatus(OrderStatus.OPEN);
    }

    @Transactional
    public StoreOrder addToCart(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        StoreOrder cart = getOpenCart().orElseGet(() -> {
            StoreOrder order = new StoreOrder();
            order.setStatus(OrderStatus.OPEN);
            return order;
        });

        OrderItem existingItem = cart.getItems().stream()
            .filter(item -> item.getProductId().equals(productId))
            .findFirst()
            .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            OrderItem item = new OrderItem(
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getUnit(),
                product.getPrice(),
                quantity
            );
            cart.addItem(item);
        }

        cart.recalculateTotal();
        return storeOrderRepository.save(cart);
    }

    @Transactional
    public StoreOrder updateCartItem(Long productId, int quantity) {
        StoreOrder cart = getOpenCart().orElseThrow(() -> new IllegalStateException("No active cart available"));
        if (quantity < 1) {
            return removeCartItem(productId);
        }

        OrderItem item = cart.getItems().stream()
            .filter(existing -> existing.getProductId().equals(productId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Item not found in cart"));

        item.setQuantity(quantity);
        cart.recalculateTotal();
        return storeOrderRepository.save(cart);
    }

    @Transactional
    public StoreOrder removeCartItem(Long productId) {
        StoreOrder cart = getOpenCart().orElseThrow(() -> new IllegalStateException("No active cart available"));
        OrderItem item = cart.getItems().stream()
            .filter(existing -> existing.getProductId().equals(productId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Item not found in cart"));
        cart.removeItem(item);
        cart.recalculateTotal();
        if (cart.getItems().isEmpty()) {
            storeOrderRepository.delete(cart);
            return cart;
        }
        return storeOrderRepository.save(cart);
    }

    @Transactional
    public StoreOrder checkout() {
        StoreOrder cart = getOpenCart().orElseThrow(() -> new IllegalStateException("No active cart available"));
        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }
        cart.setStatus(OrderStatus.COMPLETED);
        cart.setPlacedAt(LocalDateTime.now());
        cart.recalculateTotal();
        StoreOrder completed = storeOrderRepository.save(cart);
        return completed;
    }

    public List<StoreOrder> getCompletedOrders() {
        return storeOrderRepository.findByStatus(OrderStatus.COMPLETED);
    }
}
