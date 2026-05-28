package com.mystore.repository;

import com.mystore.model.OrderStatus;
import com.mystore.model.StoreOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreOrderRepository extends JpaRepository<StoreOrder, Long> {
    Optional<StoreOrder> findFirstByStatus(OrderStatus status);
    List<StoreOrder> findByStatus(OrderStatus status);
}
