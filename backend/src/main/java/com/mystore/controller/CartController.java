package com.mystore.controller;

import com.mystore.model.OrderItem;
import com.mystore.model.OrderStatus;
import com.mystore.model.StoreOrder;
import com.mystore.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart() {
        return cartService.getOpenCart()
            .map(this::mapCartResponse)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.ok(new CartResponse(List.of(), 0.0, OrderStatus.OPEN.name())));
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItem(@RequestBody CartItemRequest request) {
        StoreOrder cart = cartService.addToCart(request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(mapCartResponse(cart));
    }

    @PutMapping("/items/{productId}")
    public ResponseEntity<CartResponse> updateItem(@PathVariable Long productId,
                                                   @RequestBody QuantityRequest request) {
        StoreOrder cart = cartService.updateCartItem(productId, request.getQuantity());
        return ResponseEntity.ok(mapCartResponse(cart));
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<CartResponse> removeItem(@PathVariable Long productId) {
        StoreOrder cart = cartService.removeCartItem(productId);
        return ResponseEntity.ok(mapCartResponse(cart));
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout() {
        StoreOrder completed = cartService.checkout();
        return ResponseEntity.ok(mapOrderResponse(completed));
    }

    private CartResponse mapCartResponse(StoreOrder cart) {
        List<CartItemResponse> items = cart.getItems().stream()
            .map(this::mapCartItem)
            .collect(Collectors.toList());
        return new CartResponse(items, cart.getTotal(), cart.getStatus().name());
    }

    private CartItemResponse mapCartItem(OrderItem item) {
        return new CartItemResponse(
            item.getProductId(),
            item.getProductName(),
            item.getCategory(),
            item.getUnit(),
            item.getUnitPrice(),
            item.getQuantity(),
            item.getTotalPrice()
        );
    }

    private OrderResponse mapOrderResponse(StoreOrder order) {
        List<CartItemResponse> items = order.getItems().stream()
            .map(this::mapCartItem)
            .collect(Collectors.toList());
        return new OrderResponse(order.getId(), items, order.getTotal(), order.getPlacedAt(), order.getStatus().name());
    }

    public static class CartItemRequest {

        private Long productId;

        private int quantity = 1;

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }

    public static class QuantityRequest {

        private int quantity;

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }

    public static class CartResponse {
        private final List<CartItemResponse> items;
        private final double total;
        private final String status;

        public CartResponse(List<CartItemResponse> items, double total, String status) {
            this.items = items;
            this.total = total;
            this.status = status;
        }

        public List<CartItemResponse> getItems() {
            return items;
        }

        public double getTotal() {
            return total;
        }

        public String getStatus() {
            return status;
        }
    }

    public static class CartItemResponse {
        private final Long productId;
        private final String productName;
        private final String category;
        private final String unit;
        private final double unitPrice;
        private final int quantity;
        private final double totalPrice;

        public CartItemResponse(Long productId, String productName, String category, String unit, double unitPrice, int quantity, double totalPrice) {
            this.productId = productId;
            this.productName = productName;
            this.category = category;
            this.unit = unit;
            this.unitPrice = unitPrice;
            this.quantity = quantity;
            this.totalPrice = totalPrice;
        }

        public Long getProductId() {
            return productId;
        }

        public String getProductName() {
            return productName;
        }

        public String getCategory() {
            return category;
        }

        public String getUnit() {
            return unit;
        }

        public double getUnitPrice() {
            return unitPrice;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getTotalPrice() {
            return totalPrice;
        }
    }

    public static class OrderResponse {
        private final Long orderId;
        private final List<CartItemResponse> items;
        private final double total;
        private final java.time.LocalDateTime placedAt;
        private final String status;

        public OrderResponse(Long orderId, List<CartItemResponse> items, double total, java.time.LocalDateTime placedAt, String status) {
            this.orderId = orderId;
            this.items = items;
            this.total = total;
            this.placedAt = placedAt;
            this.status = status;
        }

        public Long getOrderId() {
            return orderId;
        }

        public List<CartItemResponse> getItems() {
            return items;
        }

        public double getTotal() {
            return total;
        }

        public java.time.LocalDateTime getPlacedAt() {
            return placedAt;
        }

        public String getStatus() {
            return status;
        }
    }
}
