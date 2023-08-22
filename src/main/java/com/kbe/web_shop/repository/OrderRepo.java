package com.kbe.web_shop.repository;

import com.kbe.web_shop.model.Order;
import com.kbe.web_shop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo  extends JpaRepository<Order, Integer> {
    List<Order> findAllByUserOrderByCreatedDateDesc(User user);

    List<Order> findAllByOrderByCreatedDateDesc();

}
