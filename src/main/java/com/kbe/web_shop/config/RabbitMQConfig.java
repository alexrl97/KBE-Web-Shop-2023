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

    @Value("direct_exchange")
    private String direct_exchange;


    @Value("product_create")
    private String product_create_queue;

    @Value("product_update")
    private String product_update_queue;

    @Value("product_delete")
    private String product_delete_queue;


    @Value("product_create_routing_key")
    private String product_create_routingKey;

    @Value("product_update_routing_key")
    private String product_update_routingKey;

    @Value("product_delete_routing_key")
    private String product_delete_routingKey;


    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(direct_exchange);
    }


    @Bean
    public Queue product_create_queue(){
        return new Queue(product_create_queue);
    }

    @Bean
    public Queue product_update_queue(){
        return new Queue(product_update_queue);
    }

    @Bean
    public Queue product_delete_queue(){
        return new Queue(product_delete_queue);
    }


    @Bean
    public Binding product_create_queue_binding(){
        return BindingBuilder
                .bind(product_create_queue())
                .to(exchange())
                .with(product_create_routingKey);
    }

    @Bean
    public Binding product_update_queue_binding(){
        return BindingBuilder
                .bind(product_update_queue())
                .to(exchange())
                .with(product_update_routingKey);
    }

    @Bean
    public Binding product_delete_queue_binding(){
        return BindingBuilder
                .bind(product_delete_queue())
                .to(exchange())
                .with(product_delete_routingKey);
    }
}
