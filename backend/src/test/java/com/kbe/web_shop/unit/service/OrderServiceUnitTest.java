package com.kbe.web_shop.unit.service;

import com.kbe.web_shop.config.constants.OrderStatus;
import com.kbe.web_shop.dto.cart.CartItemDto;
import com.kbe.web_shop.dto.cart.CartListDto;
import com.kbe.web_shop.exception.OrderNotFoundException;
import com.kbe.web_shop.model.*;
import com.kbe.web_shop.producer.CartProducer;
import com.kbe.web_shop.repository.OrderItemsRepo;
import com.kbe.web_shop.repository.OrderRepo;
import com.kbe.web_shop.service.CartService;
import com.kbe.web_shop.service.OrderService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class OrderServiceUnitTest {

    @Mock
    private CartService cartService;

    @Mock
    private CartProducer cartProducer;

    @Mock
    private AddressService addressService;

    @Mock
    private OrderRepo orderRepository;

    @Mock
    private OrderItemsRepo orderItemsRepository;

    @InjectMocks
    private OrderService orderService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateOrder() {
        User user = new User();
        String sessionId = "session_id";

        CartListDto cartListDto = new CartListDto();
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setProduct(new Product());
        cartItemDto.setQuantity(1);
        List<CartItemDto> cartItemDtoList = new ArrayList<>();
        cartItemDtoList.add(cartItemDto);
        cartListDto.setCartItems(cartItemDtoList);
        cartListDto.setTotalCost(100);

        when(cartService.listCartItems(user)).thenReturn(cartListDto);
        when(orderRepository.save(any(Order.class))).thenReturn(new Order());

        orderService.createOrder(user, sessionId);

        verify(cartProducer, times(1)).sendDeleteAllCartItemsMessage(user);
    }

    @Test
    public void testSendOrder() {
        Integer orderId = 1;
        String trackingNumber = "123456";

        Order order = new Order();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        orderService.sendOrder(orderId, trackingNumber);

        assertEquals(order.getStatus(), OrderStatus.send.toString());
        assertEquals(order.getTrackingNumber(), trackingNumber);
    }

    @Test
    public void testListOrders() {
        User user = new User();
        List<Order> orders = new ArrayList<>();

        when(orderRepository.findAllByUserOrderByCreatedDateDesc(user)).thenReturn(orders);

        List<Order> result = orderService.listOrders(user);

        assertEquals(result, orders);
    }

    @Test(expected = OrderNotFoundException.class)
    public void testGetOrderOrderNotFound() {
        Integer orderId = 1;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        orderService.getOrder(orderId);
    }

    @Test
    public void testGetOrder() {
        Integer orderId = 1;
        Order order = new Order();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        Order result = orderService.getOrder(orderId);

        assertEquals(result, order);
    }
}
