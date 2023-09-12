package com.kbe.web_shop.unit.service;

import com.kbe.web_shop.dto.user.SignUpDto;
import com.kbe.web_shop.model.Address;
import com.kbe.web_shop.model.User;
import com.kbe.web_shop.service.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class AddressServiceUnitTest {

    private AddressService addressService;

    @Mock
    private SignUpDto signUpDto;

    @Mock
    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        addressService = new AddressService();
    }

    @Test
    public void testGetAddressFromSignUpDto() {
        when(signUpDto.getFirstName()).thenReturn("John");
        when(signUpDto.getLastName()).thenReturn("Doe");
        when(signUpDto.getCompany()).thenReturn("ABC Company");
        when(signUpDto.getStreet()).thenReturn("123 Main Street");
        when(signUpDto.getHouseNumber()).thenReturn(456);
        when(signUpDto.getCity()).thenReturn("City");
        when(signUpDto.getZip()).thenReturn("12345");
        when(signUpDto.getCountry()).thenReturn("Country");

        Address resultAddress = addressService.getAddressFromSignUpDto(signUpDto, user);

        assertEquals("John", resultAddress.getFirstName());
        assertEquals("Doe", resultAddress.getLastName());
        assertEquals("ABC Company", resultAddress.getCompany());
        assertEquals("123 Main Street", resultAddress.getStreet());
        assertEquals(456, resultAddress.getHouseNumber());
        assertEquals("City", resultAddress.getCity());
        assertEquals("12345", resultAddress.getZip());
        assertEquals("Country", resultAddress.getCountry());
        assertEquals(user, resultAddress.getUser());
    }
}
