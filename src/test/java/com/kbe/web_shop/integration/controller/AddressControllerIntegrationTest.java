package com.kbe.web_shop.integration.controller;

import com.kbe.web_shop.config.constants.Role;
import com.kbe.web_shop.controller.AddressController;
import com.kbe.web_shop.dto.user.SignUpDto;
import com.kbe.web_shop.model.Address;
import com.kbe.web_shop.model.AuthenticationToken;
import com.kbe.web_shop.model.User;
import com.kbe.web_shop.repository.AddressRepo;
import com.kbe.web_shop.repository.UserRepo;
import com.kbe.web_shop.service.AddressService;
import com.kbe.web_shop.service.AuthenticationService;
import com.kbe.web_shop.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AddressControllerIntegrationTest {

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AddressController addressController;

    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private AuthenticationService authenticationService;


    @Test
    public void testUpdateAddress() throws InterruptedException {

        User user = new User();
        user = userRepo.save(user);
        AuthenticationToken authenticationToken = new AuthenticationToken(user);
        authenticationService.saveConfirmationToken(authenticationToken);
        Address address = new Address();
        address.setUser(user);
        address.setStreet("123 Main St");
        addressRepo.save(address);
        AuthenticationToken retrievedToken = authenticationService.getToken(user);
        Address savedAddress = addressService.getLatestAdressForUser(user);
        assertEquals(savedAddress.getStreet(), "123 Main St");
        savedAddress.setStreet("456 Main St");
        System.out.println(retrievedToken.getToken());
        addressController.updateAddress(savedAddress, retrievedToken.getToken());
        Thread.sleep(500);
        Address updatedAddress = addressService.getLatestAdressForUser(user);
        assertEquals(updatedAddress.getStreet(), "456 Main St");
    }
}
