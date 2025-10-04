package com.payments.srvc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.orders.srvc.dto.Order;
import com.payments.srvc.entity.Customer;
import com.payments.srvc.repository.CustomerRepository;


@Service
public class OrderManageService {

    private static final String SOURCE = "payment";
    private static final Logger LOG = LoggerFactory.getLogger(OrderManageService.class);
    private CustomerRepository customerRepository;
    private KafkaTemplate<Long, Order> kafkaTemplate;

    public OrderManageService(CustomerRepository customerRepository, KafkaTemplate<Long, Order> template) {
        this.customerRepository = customerRepository;
        this.kafkaTemplate = template;
    }

    public void reserve(Order order) {
    	long custId=order.getCustomerId();
        Customer customer = customerRepository.findById(custId).orElseThrow();
        LOG.info("Found: {}", customer);
        if (order.getPrice() < customer.getAmountAvailable()) {
            order.setStatus("ACCEPT");
            customer.setAmountReserved(customer.getAmountReserved() + order.getPrice());
            customer.setAmountAvailable(customer.getAmountAvailable() - order.getPrice());
        } else {
            order.setStatus("REJECT");
        }
        order.setSource(SOURCE);
        customerRepository.save(customer);
        kafkaTemplate.send("payment-topic", order);
        LOG.info("Sent: {}", order);
    }

    public void confirm(Order order) {
        Customer customer = customerRepository.findById(order.getCustomerId()).orElseThrow();
        LOG.info("Found: {}", customer);
        if (order.getStatus().equals("CONFIRMED")) {
            customer.setAmountReserved(customer.getAmountReserved() - order.getPrice());
            customerRepository.save(customer);
        } else if (order.getStatus().equals("ROLLBACK") && !order.getSource().equals(SOURCE)) {
            customer.setAmountReserved(customer.getAmountReserved() - order.getPrice());
            customer.setAmountAvailable(customer.getAmountAvailable() + order.getPrice());
            customerRepository.save(customer);
        }
    }
}
