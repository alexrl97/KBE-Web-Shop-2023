package com.kbe.web_shop.repository;

import com.kbe.web_shop.model.Address;
import com.kbe.web_shop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AddressRepo extends JpaRepository<Address, Integer> {
    Address findTopByUserOrderByCreatedDateDesc(User user);
}
