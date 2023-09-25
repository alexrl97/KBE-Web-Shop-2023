package com.kbe.web_shop.integration.service;

import com.kbe.web_shop.dto.cart.CartDto;
import com.kbe.web_shop.dto.checkout.CheckoutItemDto;
import com.kbe.web_shop.exception.OrderNotFoundException;
import com.kbe.web_shop.model.*;
import com.kbe.web_shop.repository.CategoryRepo;
import com.kbe.web_shop.repository.OrderItemsRepo;
import com.kbe.web_shop.repository.ProductRepo;
import com.kbe.web_shop.repository.UserRepo;
import com.kbe.web_shop.service.CartService;
import com.kbe.web_shop.service.OrderService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderServiceIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AddressService addressService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderItemsRepo orderItemsRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    private User user;

    private CartDto cartDto1;

    @Test
    public void testCreateSession() throws StripeException {

        List<CheckoutItemDto> checkoutItemDtoList = new ArrayList<>();
        CheckoutItemDto checkoutItemDto= new CheckoutItemDto();
        checkoutItemDto.setPrice(100);
        checkoutItemDto.setQuantity(1);
        checkoutItemDto.setProductName("test");
        checkoutItemDto.setUserId(1);
        checkoutItemDto.setProductId(1);
        checkoutItemDtoList.add(checkoutItemDto);

        Session session = orderService.createSession(checkoutItemDtoList);

        assertEquals(session.getCancelUrl(), "http://localhost:8081/payment/failed");
        assertEquals(session.getSuccessUrl(), "http://localhost:8081/payment/success");
        assertEquals(session.getAmountTotal(), 10000);
        assertNotNull(session.getUrl());
    }

    @Test
    public void createOrderTest(){

        Order order = createNewTestingUserAndOrder();
        assertNotNull(order);
        assertEquals(order.getUser().getEmail(), user.getEmail());
        assertEquals(order.getTotalPrice(), 19.99);

        OrderItem orderItem = orderItemsRepo.findAll().stream().filter(X -> Objects.equals(X.getOrder().getId(), order.getId())).toList().get(0);

        assertEquals(orderItem.getProduct().getId(), cartDto1.getProductId());
    }

    @Test
    public void testSendOrder(){
        Order order = createNewTestingUserAndOrder();
        order = orderService.getOrder(order.getId());
        assertEquals(order.getStatus(), "pending");
        orderService.sendOrder(order.getId(), "1234567890");
        order = orderService.getOrder(order.getId());
        assertEquals(order.getTrackingNumber(), "1234567890");
        assertEquals(order.getStatus(), "send");
    }


    @Test
    public void testGetOrderReturnsOrder(){
        Order order = createNewTestingUserAndOrder();
        Order getOrder = orderService.getOrder(order.getId());
        assertEquals(order.getId(), getOrder.getId());
    }

    @Test
    public void testGetOrderThrowsOrderNotFoundExceptionIfNotPresent(){
        assertThrows(OrderNotFoundException.class, () -> {
            orderService.getOrder(Integer.MAX_VALUE);
        });
    }

    private Order createNewTestingUserAndOrder(){
        user = new User();
        Random random = new Random();
        String generatedString = random.ints(92, 123 + 1)
                .limit(10)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        String email = generatedString + "@mail.de";
        user.setEmail(email);
        userRepo.save(user);
        user = userRepo.findByEmail(email);

        Address address = new Address();
        address.setUser(user);
        addressService.createUpdateAddress(address);

        cartDto1 = new CartDto();
        cartDto1.setId(1000);
        cartDto1.setQuantity(1);
        cartDto1.setUser(user);
        cartDto1.setProductId(getTestProduct().getId());
        cartService.addToCart(cartDto1);

        orderService.createOrder(user, "sessionId");

        return orderService.listOrders(user).get(0);
    }

    private Product getTestProduct(){
        Category category = new Category();
        category.setCategoryName("Test Category");
        category.setId(100);
        category = categoryRepo.findFirstByOrderByIdDesc();

        Product product1 = new Product();
        product1.setName("Product 1");
        product1.setDescription("Description 1");
        product1.setImageURL("image1.jpg");
        product1.setPrice(19.99);
        product1.setDeckCardId("12345");
        product1.setRarity("Common");
        product1.setCategory(category);
        productRepo.save(product1);

        List<Product> allProducts = productRepo.findAll();

        return allProducts.stream()
                .filter(p -> p.getName().equals("Product 1"))
                .findFirst()
                .orElse(null);
    }
}
