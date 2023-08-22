package com.kbe.web_shop.service;

import com.kbe.web_shop.config.constants.OrderStatus;
import com.kbe.web_shop.dto.cart.CartDto;
import com.kbe.web_shop.dto.cart.CartItemDto;
import com.kbe.web_shop.dto.checkout.CheckoutItemDto;
import com.kbe.web_shop.exception.OrderNotFoundException;
import com.kbe.web_shop.model.Order;
import com.kbe.web_shop.model.OrderItem;
import com.kbe.web_shop.model.User;
import com.kbe.web_shop.repository.OrderItemsRepo;
import com.kbe.web_shop.repository.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Value("${BASE_URL}")
    private String baseURL;

    @Value("${STRIPE_SECRET_KEY}")
    private String apiKey;

    @Autowired
    private CartService cartService;

    @Autowired
    private AddressService addressService;

    @Autowired
    OrderRepo orderRepository;

    @Autowired
    OrderItemsRepo orderItemsRepository;

    public Session createSession(List<CheckoutItemDto> checkoutItemDtoList) throws StripeException {

        // sucess and failure urls

        String successURL = baseURL + "payment/success";

        String failureURL = baseURL + "payment/failed";

        Stripe.apiKey = apiKey;

        List<SessionCreateParams.LineItem> sessionItemList = new ArrayList<>();


        for (CheckoutItemDto checkoutItemDto: checkoutItemDtoList) {
            sessionItemList.add(createSessionLineItem(checkoutItemDto));
        }

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCancelUrl(failureURL)
                .addAllLineItem(sessionItemList)
                .setSuccessUrl(successURL)
                .build();
        return Session.create(params);
    }

    private SessionCreateParams.LineItem createSessionLineItem(CheckoutItemDto checkoutItemDto) {

        return SessionCreateParams.LineItem.builder()
                .setPriceData(createPriceData(checkoutItemDto))
                .setQuantity(Long.parseLong(String.valueOf(checkoutItemDto.getQuantity())))
                .build();

    }

    private SessionCreateParams.LineItem.PriceData createPriceData(CheckoutItemDto checkoutItemDto) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("eur")
                .setUnitAmount((long)(checkoutItemDto.getPrice()*100))
                .setProductData(
                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName(checkoutItemDto.getProductName())
                                .build()
                ).build();
    }

    public void placeOrder(User user, String sessionId) {
        // first let get cart items for the user
        CartDto cartDto = cartService.listCartItems(user);

        List<CartItemDto> cartItemDtoList = cartDto.getCartItems();

        // create the order and save it
        Order newOrder = new Order();
        newOrder.setCreatedDate(new Date());
        newOrder.setSessionId(sessionId);
        newOrder.setUser(user);
        newOrder.setTotalPrice(cartDto.getTotalCost());
        newOrder.setStatus(OrderStatus.pending.toString());
        newOrder.setAddress(addressService.getLatestAdressForUser(user));
        orderRepository.save(newOrder);

        for (CartItemDto cartItemDto : cartItemDtoList) {
            // create orderItem and save each one
            OrderItem orderItem = new OrderItem();
            orderItem.setCreatedDate(new Date());
            orderItem.setPrice(cartItemDto.getProduct().getPrice());
            orderItem.setProduct(cartItemDto.getProduct());
            orderItem.setQuantity(cartItemDto.getQuantity());
            orderItem.setOrder(newOrder);
            // add to order item list
            orderItemsRepository.save(orderItem);
        }

        cartService.deleteUserCartItems(user);
    }

    public void sendOrder(Integer orderId, String trackingNumber) {
        Order sendOrder = getOrder(orderId);
        sendOrder.setStatus(OrderStatus.send.toString());
        sendOrder.setTrackingNumber(trackingNumber);
        orderRepository.save(sendOrder);
    }

    public List<Order> listOrders(User user) {
        return orderRepository.findAllByUserOrderByCreatedDateDesc(user);
    }

    public Order getOrder(Integer orderId) throws OrderNotFoundException {

        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            return order.get();
        }
        throw new OrderNotFoundException("Order not found");
    }

    public List<Order> listAllOrders() {
        return orderRepository.findAllByOrderByCreatedDateDesc();
    }
}
