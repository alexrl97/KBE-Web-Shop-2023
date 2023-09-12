package com.kbe.web_shop.controller;

import com.kbe.web_shop.common.ApiResponse;
import com.kbe.web_shop.model.Address;
import com.kbe.web_shop.model.User;
import com.kbe.web_shop.producer.AddressProducer;
import com.kbe.web_shop.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private AddressProducer addressProducer;

    @PostMapping("/update")
    public ResponseEntity<ApiResponse> updateAddress(@RequestBody Address address, @RequestParam("token") String token) {
        // authenticate the token
        authenticationService.authenticate(token);

        // find the user

        User user = authenticationService.getUser(token);
        address.setUser(user);

        //addressService.createUpdateAddress(address);
        addressProducer.sendCreateUpdateAddressMessage(address);

        return new ResponseEntity<>(new ApiResponse(true, "Address Updated"), HttpStatus.CREATED);
    }
}
