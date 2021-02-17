package com.galbern.req.sandbox;

import org.springframework.stereotype.Service;

import java.util.List;

//@Service
public interface CustomerService {
    public Customer addCustomer(Customer c);
    public List<Customer> getCustomers();
    public Customer editCustomer(Long id);
    public Customer deleteCustomer(Long id);


    Customer findCustomerById(Long id);
}
