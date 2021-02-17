package com.galbern.req.controller;

import com.galbern.req.jpa.entities.Address;
import com.galbern.req.exception.AddressNotFoundException;
import com.galbern.req.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/v1/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;



    @PostMapping
    public String addAddress(@RequestBody Address address){
        Address a = addressService.addAddress(address);
        return  a.toString() + "saved ";

    }
    @GetMapping
    public Set<Address> getAddresses(@RequestParam(value="addressId", required = false) List<Long> addresses){
        return  addressService.getAddresses(addresses);

    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public ResponseEntity<Address> findAddress(@PathVariable("id") Long id){
        var result = addressService.findAddressById(id);
        if(result== null) throw new AddressNotFoundException("Address with "+ id +" not found");
        else
            return  new ResponseEntity<>(result, HttpStatus.FOUND);

    }
}
