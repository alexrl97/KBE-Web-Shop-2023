package com.kbe.web_shop.unit.controller;

import com.kbe.web_shop.controller.AddressController;
import com.kbe.web_shop.model.Address;
import com.kbe.web_shop.service.AuthenticationService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class AddressControllerUnitTest {

    @Mock
    private AuthenticationService authenticationService;

    private AddressController addressController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        addressController = new AddressController();
    }

    @Test(expected = Exception.class)
    public void testUpdateAddressAuthenticationFailure() {
        Address address = new Address();
        address.setStreet("123 Main St");
        String token = "invalidToken";

        doThrow(new Exception("Authentication failed")).when(authenticationService).authenticate(token);

        addressController.updateAddress(address, token);
    }
}
