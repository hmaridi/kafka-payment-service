package com.payments.srvc.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Service;
import com.payments.srvc.entity.Customer;
import com.payments.srvc.repository.CustomerRepository;
import net.datafaker.Faker;

@Service
public class CustomerService {
	
	private final CustomerRepository customerRepository;

	public CustomerService(CustomerRepository customerRepository) {
		super();
		this.customerRepository = customerRepository;
	}

	public List<Customer> generateAndSave(int count) {
	    Random r = new Random();
	    Faker faker = new Faker();
	    List<Customer> customerList = new ArrayList<>();
	    for (int i = 0; i < count; i++) {
	        int x = r.nextInt(100, 1000);
	        Customer c = new Customer(null,faker.name().fullName(),faker.internet().emailAddress(),faker.phoneNumber().phoneNumber(),x,0);
	        customerList.add(c);
	    }
	    return customerRepository.saveAll(customerList);
	}

}
	
	
