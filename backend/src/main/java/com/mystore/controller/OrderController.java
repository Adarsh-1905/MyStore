package com.mystore.controller;

import com.mystore.model.StoreOrder;
import com.mystore.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final CartService cartService;

    public OrderController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<List<CartController.OrderResponse>> getOrders() {
        List<CartController.OrderResponse> orders = cartService.getCompletedOrders().stream()
            .map(this::mapOrderResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    private CartController.OrderResponse mapOrderResponse(StoreOrder order) {
        var items = order.getItems().stream()
            .map(item -> new CartController.CartItemResponse(
                item.getProductId(),
                item.getProductName(),
                item.getCategory(),
                item.getUnit(),
                item.getUnitPrice(),
                item.getQuantity(),
                item.getTotalPrice()
            ))
            .collect(Collectors.toList());

        return new CartController.OrderResponse(order.getId(), items, order.getTotal(), order.getPlacedAt(), order.getStatus().name());
    }
}
