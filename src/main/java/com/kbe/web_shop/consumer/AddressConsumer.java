package com.kbe.web_shop.consumer;

import com.kbe.web_shop.model.Address;
import com.kbe.web_shop.service.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddressConsumer.class);

    @Autowired
    AddressService addressService;


    @RabbitListener(queues = {"${address_create_update_queue}"})
    public void consumeCreateAddressMessage(String message){
        LOGGER.info(String.format("Received create/update message for address -> %s", message));
        Address address = Address.fromJsonString(message);
        addressService.createUpdateAddress(address);
    }
}
