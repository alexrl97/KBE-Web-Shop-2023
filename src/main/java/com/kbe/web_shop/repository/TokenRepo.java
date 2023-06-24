package com.kbe.web_shop.repository;

import com.kbe.web_shop.model.AuthenticationToken;
import com.kbe.web_shop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepo extends JpaRepository<AuthenticationToken, Integer> {

    AuthenticationToken findByUser(User user);
}
