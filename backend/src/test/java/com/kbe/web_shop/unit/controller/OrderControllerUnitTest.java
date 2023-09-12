package com.kbe.web_shop.unit.controller;
import com.kbe.web_shop.common.ApiResponse;
import com.kbe.web_shop.controller.OrderController;
import com.kbe.web_shop.dto.checkout.CheckoutItemDto;
import com.kbe.web_shop.dto.order.OrderCreateDto;
import com.kbe.web_shop.exception.AuthenticationFailException;
import com.kbe.web_shop.exception.OrderNotFoundException;
import com.kbe.web_shop.model.Order;
import com.kbe.web_shop.model.User;
import com.kbe.web_shop.producer.OrderProducer;
import com.kbe.web_shop.service.AuthenticationService;
import com.kbe.web_shop.service.OrderService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class OrderControllerUnitTest {

    @Mock
    private OrderService orderService;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private OrderProducer orderProducer;

    @InjectMocks
    private OrderController orderController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateOrderSuccess() {
        String token = "validToken";
        String sessionId = "123456";

        when(authenticationService.getUser(token)).thenReturn(new User());

        ResponseEntity<ApiResponse> response = orderController.createOrder(token, sessionId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Order has been placed", response.getBody().getMessage());

        verify(orderProducer, times(1)).sendCreateOrderMessage(any(OrderCreateDto.class));
    }

    @Test(expected = AuthenticationFailException.class)
    public void testCreateOrderAuthenticationFailure() {
        String token = "invalidToken";
        String sessionId = "123456";

        doThrow(new AuthenticationFailException("Authentication failed")).when(authenticationService).authenticate(token);

        orderController.createOrder(token, sessionId);
    }

    @Test
    public void testGetOrdersByUserSuccess() {
        String token = "validToken";
        User user = new User();
        List<Order> orders = new ArrayList<>();

        when(authenticationService.getUser(token)).thenReturn(user);
        when(orderService.listOrders(user)).thenReturn(orders);

        ResponseEntity<List<Order>> response = orderController.getAllOrdersByUser(token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orders, response.getBody());
    }

    @Test(expected = AuthenticationFailException.class)
    public void testGetOrdersByUserAuthenticationFailure() {
        String token = "invalidToken";

        doThrow(new AuthenticationFailException("Authentication failed")).when(authenticationService).authenticate(token);

        orderController.getAllOrdersByUser(token);
    }

    @Test
    public void testGetOrderByIdSuccess() {
        String token = "validToken";
        int orderId = 1;
        Order order = new Order();

        when(authenticationService.getUser(token)).thenReturn(new User());
        when(orderService.getOrder(orderId)).thenReturn(order);

        ResponseEntity<Object> response = orderController.getOrderById(orderId, token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test(expected = AuthenticationFailException.class)
    public void testGetOrderByIdAuthenticationFailure() {
        String token = "invalidToken";
        int orderId = 1;

        doThrow(new AuthenticationFailException("Authentication failed")).when(authenticationService).authenticate(token);

        orderController.getOrderById(orderId, token);
    }

}
