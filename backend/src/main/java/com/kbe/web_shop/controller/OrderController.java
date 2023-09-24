package com.kbe.web_shop.controller;

import com.kbe.web_shop.config.common.ApiResponse;
import com.kbe.web_shop.dto.checkout.CheckoutItemDto;
import com.kbe.web_shop.dto.checkout.StripeResponse;
import com.kbe.web_shop.dto.order.OrderCreateDto;
import com.kbe.web_shop.dto.order.OrderSendDto;
import com.kbe.web_shop.exception.AuthenticationFailException;
import com.kbe.web_shop.exception.OrderNotFoundException;
import com.kbe.web_shop.model.Order;
import com.kbe.web_shop.model.User;
import com.kbe.web_shop.producer.OrderProducer;
import com.kbe.web_shop.service.AuthenticationService;
import com.kbe.web_shop.service.OrderService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private OrderService orderService;

    @Autowired
    OrderProducer orderProducer;

    // stripe session checkout api

    @PostMapping("/create-checkout-session")
    public ResponseEntity<StripeResponse> checkoutList(@RequestBody List<CheckoutItemDto> checkoutItemDtoList)
            throws StripeException {
        Session session = orderService.createSession(checkoutItemDtoList);
        StripeResponse stripeResponse = new StripeResponse(session.getId());
        return new ResponseEntity<>(stripeResponse, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createOrder(@RequestParam("token") String token, @RequestParam("sessionId") String sessionId)
            throws AuthenticationFailException {
        // validate token
        authenticationService.authenticate(token);
        // retrieve user
        User user = authenticationService.getUser(token);
        // place the order
        //orderService.createOrder(user, sessionId);
        orderProducer.sendCreateOrderMessage(new OrderCreateDto(sessionId, user));

        return new ResponseEntity<>(new ApiResponse(true, "Order has been placed"), HttpStatus.CREATED);
    }

    @PostMapping("/send/{orderId}")
    public ResponseEntity<ApiResponse> sendOrder(@RequestParam("token") String token, @PathVariable("orderId") Integer orderId, @RequestParam("trackingNumber") String trackingNumber){
        if(authenticationService.hasEditPermission(token)) {
            if (trackingNumber.length() == 0)
                return new ResponseEntity<>(new ApiResponse(false, "Tracking number empty"), HttpStatus.BAD_REQUEST);
            else {
                //orderService.sendOrder(orderId, trackingNumber);
                orderProducer.sendSendOrderMessage(new OrderSendDto(orderId, trackingNumber));
                return new ResponseEntity<>(new ApiResponse(true, "Order send"), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new ApiResponse(false, "Only Storehouse workers can send out orders"), HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/")
    public ResponseEntity<List<Order>> getAllOrdersByUser(@RequestParam("token") String token) throws AuthenticationFailException {
        // validate token
        authenticationService.authenticate(token);
        // retrieve user
        User user = authenticationService.getUser(token);
        List<Order> orderDtoList;

        if(authenticationService.hasEditPermission(token)){
            orderDtoList = orderService.listAllOrders();
        }
        else {
            orderDtoList = orderService.listOrders(user);
        }

        return new ResponseEntity<>(orderDtoList, HttpStatus.OK);
    }


    // get orderitems for an order
    @GetMapping("/{id}")
    public ResponseEntity<Object> getOrderById(@PathVariable("id") Integer id, @RequestParam("token") String token)
            throws AuthenticationFailException {
        // validate token
        authenticationService.authenticate(token);
        try {
            Order order = orderService.getOrder(id);
            return new ResponseEntity<>(order,HttpStatus.OK);
        }
        catch (OrderNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }

    }

}
