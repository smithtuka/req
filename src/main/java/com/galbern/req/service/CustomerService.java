package com.galbern.req.service;

import com.galbern.req.domain.Customer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface CustomerService {
    public Customer addCustomer(Customer c);
    public List<Customer> getCustomers();
    public Customer editCustomer(Long id);
    public Customer deleteCustomer(Long id);


    Customer findCustomerById(Long id);
}
