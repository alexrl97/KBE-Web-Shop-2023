package com.kbe.web_shop.repository;

import com.kbe.web_shop.model.Cart;
import com.kbe.web_shop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepo extends JpaRepository<Cart, Integer> {

    List<Cart> findAllByUserOrderByCreatedDateDesc(User user);

    void deleteByUser(User user);
}
