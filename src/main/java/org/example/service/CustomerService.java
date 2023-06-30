package org.example.service;

import org.example.entitie.Customer;
import org.example.processor.Benchmark;
import org.example.repository.Repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class CustomerService {

    private Repo<Customer> customerRepo;

    @Autowired
    public void setCustomerRepo(Repo<Customer> customerRepo) {
	this.customerRepo = customerRepo;
    }

    @Benchmark
    public Customer get(Long id) {
	return customerRepo.getById(id);
    }

    @Benchmark
    public void update(Customer customer) {
	customerRepo.update(customer);
    }





}