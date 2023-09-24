package com.kbe.web_shop.unit.controller;

import com.kbe.web_shop.config.common.ApiResponse;
import com.kbe.web_shop.controller.CartController;
import com.kbe.web_shop.dto.cart.CartDto;
import com.kbe.web_shop.dto.cart.CartListDto;
import com.kbe.web_shop.exception.AuthenticationFailException;
import com.kbe.web_shop.model.User;
import com.kbe.web_shop.producer.CartProducer;
import com.kbe.web_shop.service.AuthenticationService;
import com.kbe.web_shop.service.CartService;
import com.kbe.web_shop.service.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CartControllerUnitTest {

    @Mock
    private CartService cartService;

    @Mock
    private ProductService productService;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private CartProducer cartProducer;

    @InjectMocks
    private CartController cartController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddToCart_Success() {
        CartDto cartDto = new CartDto();
        User user = new User();
        String token = "validToken";

        when(authenticationService.getUser(token)).thenReturn(user);

        ResponseEntity<ApiResponse> response = cartController.addToCart(cartDto, token);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Added to cart", response.getBody().getMessage());

        verify(cartProducer, times(1)).sendAddToCartMessage(cartDto);
    }

    @Test(expected = AuthenticationFailException.class)
    public void testAddToCart_AuthenticationFailure() {
        CartDto cartDto = new CartDto();
        String token = "invalidToken";

        doThrow(new AuthenticationFailException("Authentication failed")).when(authenticationService).authenticate(token);

        cartController.addToCart(cartDto, token);
    }

    @Test
    public void testGetCartItems_Success() {
        User user = new User();
        String token = "validToken";

        when(authenticationService.getUser(token)).thenReturn(user);
        when(cartService.listCartItems(user)).thenReturn(new CartListDto());

        ResponseEntity<CartListDto> response = cartController.getCartItems(token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test(expected = AuthenticationFailException.class)
    public void testGetCartItems_AuthenticationFailure() {
        String token = "invalidToken";

        doThrow(new AuthenticationFailException("Authentication failed")).when(authenticationService).authenticate(token);

        cartController.getCartItems(token);
    }


    @Test(expected = AuthenticationFailException.class)
    public void testDeleteCartItem_AuthenticationFailure() {
        Integer itemId = 1;
        String token = "invalidToken";

        doThrow(new AuthenticationFailException("Authentication failed")).when(authenticationService).authenticate(token);

        cartController.deleteCartItem(itemId, token);
    }
}