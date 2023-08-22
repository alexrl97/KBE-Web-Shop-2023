package com.kbe.web_shop.repository;

import com.kbe.web_shop.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsRepo extends JpaRepository<OrderItem,Integer> {
}
