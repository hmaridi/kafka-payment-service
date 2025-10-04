package com.payments.srvc.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.orders.srvc.dto.Order;
import com.payments.srvc.service.OrderManageService;



@Service
public class PaymentOrderConsumer {
	
	 private static final Logger LOG = LoggerFactory.getLogger(PaymentOrderConsumer.class);
	 
	@Autowired
	private OrderManageService orderManageService;
	
	@KafkaListener(id = "orders", topics = "orders-topic", groupId = "payment")
    public void onEvent(Order o) {
        LOG.info("Received: {}" , o);
        if (o.getStatus().equals("NEW"))
            orderManageService.reserve(o);
        else
            orderManageService.confirm(o);
    }

}
