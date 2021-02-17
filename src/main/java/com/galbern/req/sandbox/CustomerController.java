package com.galbern.req.sandbox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

//@RestController
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<Customer> addCustomer(@RequestBody Customer customer){
        return  new ResponseEntity<>(customerService.addCustomer(customer), HttpStatus.ACCEPTED);
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getCustomers(){
        return new ResponseEntity<>(customerService.getCustomers(), HttpStatus.FOUND);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public ResponseEntity<Customer> findCustomerById(@PathVariable("id") Long id){
        try{
            return new ResponseEntity<Customer>(customerService.findCustomerById(id), HttpStatus.FOUND);
        }
        catch(RuntimeException e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "customer with id = "+ id + " not found", e);
        }
    }


}
