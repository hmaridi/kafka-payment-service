package com.payments.srvc.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.payments.srvc.entity.Customer;
import com.payments.srvc.service.CustomerService;

@RestController
@RequestMapping("/customer")
public class PaymentController {

	private final CustomerService customerService;
	
	public PaymentController(CustomerService customerService) {
		super();
		this.customerService = customerService;
	}

	@GetMapping("/generate/{count}")
    public List<Customer> generateData(@PathVariable("count") int count) {
        return customerService.generateAndSave(count);
    }
}
