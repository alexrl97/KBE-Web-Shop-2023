package com.kbe.web_shop.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${direct_exchange}")
    private String directExchange;

    @Value("${product_create_queue}")
    private String productCreateQueue;

    @Value("${product_update_queue}")
    private String productUpdateQueue;

    @Value("${product_delete_queue}")
    private String productDeleteQueue;

    @Value("${category_create_queue}")
    private String categoryCreateQueue;

    @Value("${category_update_queue}")
    private String categoryUpdateQueue;

    @Value("${category_delete_queue}")
    private String categoryDeleteQueue;

    @Value("${wishlist_create_queue}")
    private String wishlistCreateQueue;

    @Value("${wishlist_delete_queue}")
    private String wishlistDeleteQueue;

    @Value("${address_create_update_queue}")
    private String addressCreateUpdateQueue;

    @Value("${cart_add_queue}")
    private String cartAddQueue;

    @Value("${cart_update_queue}")
    private String cartUpdateQueue;

    @Value("${cart_delete_queue}")
    private String cartDeleteQueue;

    @Value("${cart_delete_all_queue}")
    private String cartDeleteAllQueue;

    @Value("${create_order_queue}")
    private String createOrderQueue;

    @Value("${send_order_queue}")
    private String sendOrderQueue;

    @Value("${authentication_token_create_queue}")
    private String authenticationTokenCreateQueue;

    @Value("${user_create_queue}")
    private String userCreateQueue;

    @Value("${product_create_routing_key}")
    private String productCreateRoutingKey;

    @Value("${product_update_routing_key}")
    private String productUpdateRoutingKey;

    @Value("${product_delete_routing_key}")
    private String productDeleteRoutingKey;

    @Value("${category_create_routing_key}")
    private String categoryCreateRoutingKey;

    @Value("${category_update_routing_key}")
    private String categoryUpdateRoutingKey;

    @Value("${category_delete_routing_key}")
    private String categoryDeleteRoutingKey;

    @Value("${wishlist_create_routing_key}")
    private String wishlistCreateRoutingKey;

    @Value("${wishlist_delete_routing_key}")
    private String wishlistDeleteRoutingKey;

    @Value("${address_create_update_routing_key}")
    private String addressCreateUpdateRoutingKey;

    @Value("${cart_add_routing_key}")
    private String cartAddRoutingKey;

    @Value("${cart_update_routing_key}")
    private String cartUpdateRoutingKey;

    @Value("${cart_delete_routing_key}")
    private String cartDeleteRoutingKey;

    @Value("${cart_delete_all_routing_key}")
    private String cartDeleteAllRoutingKey;

    @Value("${create_order_routing_key}")
    private String createOrderRoutingKey;

    @Value("${send_order_routing_key}")
    private String sendOrderRoutingKey;

    @Value("${authentication_token_create_routing_key}")
    private String authenticationTokenCreateRoutingKey;

    @Value("${user_create_routing_key}")
    private String userCreateRoutingKey;


    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(directExchange);
    }

    @Bean
    public Queue productCreateQueue() {
        return new Queue(productCreateQueue);
    }

    @Bean
    public Queue productUpdateQueue() {
        return new Queue(productUpdateQueue);
    }

    @Bean
    public Queue productDeleteQueue() {
        return new Queue(productDeleteQueue);
    }

    @Bean
    public Queue categoryCreateQueue() {
        return new Queue(categoryCreateQueue);
    }

    @Bean
    public Queue categoryUpdateQueue() {
        return new Queue(categoryUpdateQueue);
    }

    @Bean
    public Queue categoryDeleteQueue() {
        return new Queue(categoryDeleteQueue);
    }

    @Bean
    public Queue wishlistCreateQueue() {
        return new Queue(wishlistCreateQueue);
    }

    @Bean
    public Queue wishlistDeleteQueue() {
        return new Queue(wishlistDeleteQueue);
    }

    @Bean
    public Queue addressCreateUpdateQueue() {
        return new Queue(addressCreateUpdateQueue);
    }

    @Bean
    public Queue cartAddQueue() {
        return new Queue(cartAddQueue);
    }

    @Bean
    public Queue cartUpdateQueue() {
        return new Queue(cartUpdateQueue);
    }

    @Bean
    public Queue cartDeleteQueue() {
        return new Queue(cartDeleteQueue);
    }

    @Bean
    public Queue cartDeleteAllQueue() {
        return new Queue(cartDeleteAllQueue);
    }

    @Bean
    public Queue createOrderQueue() {
        return new Queue(createOrderQueue);
    }

    @Bean
    public Queue sendOrderQueue() {
        return new Queue(sendOrderQueue);
    }

    @Bean
    public Queue authenticationTokenCreateQueue() {
        return new Queue(authenticationTokenCreateQueue);
    }

    @Bean
    public Queue userCreateQueue() {
        return new Queue(userCreateQueue);
    }

    @Bean
    public Binding productCreateBinding(Queue productCreateQueue, TopicExchange exchange) {
        return BindingBuilder.bind(productCreateQueue).to(exchange).with(productCreateRoutingKey);
    }

    @Bean
    public Binding productUpdateBinding(Queue productUpdateQueue, TopicExchange exchange) {
        return BindingBuilder.bind(productUpdateQueue).to(exchange).with(productUpdateRoutingKey);
    }

    @Bean
    public Binding productDeleteBinding(Queue productDeleteQueue, TopicExchange exchange) {
        return BindingBuilder.bind(productDeleteQueue).to(exchange).with(productDeleteRoutingKey);
    }

    @Bean
    public Binding categoryCreateBinding(Queue categoryCreateQueue, TopicExchange exchange) {
        return BindingBuilder.bind(categoryCreateQueue).to(exchange).with(categoryCreateRoutingKey);
    }

    @Bean
    public Binding categoryUpdateBinding(Queue categoryUpdateQueue, TopicExchange exchange) {
        return BindingBuilder.bind(categoryUpdateQueue).to(exchange).with(categoryUpdateRoutingKey);
    }

    @Bean
    public Binding categoryDeleteBinding(Queue categoryDeleteQueue, TopicExchange exchange) {
        return BindingBuilder.bind(categoryDeleteQueue).to(exchange).with(categoryDeleteRoutingKey);
    }

    @Bean
    public Binding wishlistCreateBinding(Queue wishlistCreateQueue, TopicExchange exchange) {
        return BindingBuilder.bind(wishlistCreateQueue).to(exchange).with(wishlistCreateRoutingKey);
    }

    @Bean
    public Binding wishlistDeleteBinding(Queue wishlistDeleteQueue, TopicExchange exchange) {
        return BindingBuilder.bind(wishlistDeleteQueue).to(exchange).with(wishlistDeleteRoutingKey);
    }

    @Bean
    public Binding addressCreateBinding(Queue addressCreateUpdateQueue, TopicExchange exchange) {
        return BindingBuilder.bind(addressCreateUpdateQueue).to(exchange).with(addressCreateUpdateRoutingKey);
    }

    @Bean
    public Binding cartAddBinding(Queue cartAddQueue, TopicExchange exchange) {
        return BindingBuilder.bind(cartAddQueue).to(exchange).with(cartAddRoutingKey);
    }

    @Bean
    public Binding cartUpdateBinding(Queue cartUpdateQueue, TopicExchange exchange) {
        return BindingBuilder.bind(cartUpdateQueue).to(exchange).with(cartUpdateRoutingKey);
    }

    @Bean
    public Binding cartDeleteBinding(Queue cartDeleteQueue, TopicExchange exchange) {
        return BindingBuilder.bind(cartDeleteQueue).to(exchange).with(cartDeleteRoutingKey);
    }

    @Bean
    public Binding cartDeleteAllBinding(Queue cartDeleteAllQueue, TopicExchange exchange) {
        return BindingBuilder.bind(cartDeleteAllQueue).to(exchange).with(cartDeleteAllRoutingKey);
    }

    @Bean
    public Binding createOrderBinding(Queue createOrderQueue, TopicExchange exchange) {
        return BindingBuilder.bind(createOrderQueue).to(exchange).with(createOrderRoutingKey);
    }

    @Bean
    public Binding sendOrderBinding(Queue sendOrderQueue, TopicExchange exchange) {
        return BindingBuilder.bind(sendOrderQueue).to(exchange).with(sendOrderRoutingKey);
    }

    @Bean
    public Binding authenticationTokenCreateBinding(Queue authenticationTokenCreateQueue, TopicExchange exchange) {
        return BindingBuilder.bind(authenticationTokenCreateQueue).to(exchange).with(authenticationTokenCreateRoutingKey);
    }

    @Bean
    public Binding userCreateBinding(Queue userCreateQueue, TopicExchange exchange) {
        return BindingBuilder.bind(userCreateQueue).to(exchange).with(userCreateRoutingKey);
    }
}
