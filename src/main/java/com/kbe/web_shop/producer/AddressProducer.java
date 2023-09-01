package com.kbe.web_shop.producer;

import com.kbe.web_shop.model.Address; // Import the Address entity
import com.kbe.web_shop.utils.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AddressProducer {

    @Value("${direct_exchange}")
    private String directExchange;

    @Value("${address_create_update_routing_key}")
    private String addressCreateUpdateRoutingKey;


    private static final Logger LOGGER = LoggerFactory.getLogger(AddressProducer.class);

    private final RabbitTemplate rabbitTemplate;

    public AddressProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendCreateUpdateAddressMessage(Address address) {
        String message = Helper.toJsonString(address);
        LOGGER.info("Sending create message for address -> {}", message);
        rabbitTemplate.convertAndSend(directExchange, addressCreateUpdateRoutingKey, message);
    }
}