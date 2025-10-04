package com.payments.srvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.payments.srvc.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
