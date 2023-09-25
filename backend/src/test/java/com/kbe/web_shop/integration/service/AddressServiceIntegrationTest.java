package com.kbe.web_shop.integration.service;

import com.kbe.web_shop.model.Address;
import com.kbe.web_shop.repository.UserRepo;
import com.kbe.web_shop.model.User;
import com.kbe.web_shop.service.AddressService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AddressServiceIntegrationTest {

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserRepo userRepo;


    @Test
    public void testCreateAddressWithoutID() {
        Address address = new Address();
        address.setId(null);
        User user = new User();
        userRepo.save(user);
        address.setStreet("123 Main St");
        address.setUser(user);
        addressService.createUpdateAddress(address);
        Address savedAddress = addressService.getLatestAdressForUser(user);
        assertNotNull(savedAddress.getId());
        assertEquals(savedAddress.getStreet(), "123 Main St");
    }
}
