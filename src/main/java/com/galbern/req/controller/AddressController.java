package com.galbern.req.controller;

import com.galbern.req.domain.Address;
import com.galbern.req.exception.AddressNotFoundException;
import com.galbern.req.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;



    @PostMapping("/")
    public String addAddress(@RequestBody Address address){
        Address a = addressService.addAddress(address);
        return  a.toString() + "saved ";

    }
    @GetMapping("/")
    public Set<Address> getAddresses(){
        return  addressService.getAddresses();

    }
//    private RequestContext requestContext; // webRequest equivalent ?
//    private WebRequest webRequest;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public ResponseEntity<Address> findAddress(@PathVariable("id") Long id){
        var result = addressService.findAddressById(id);
        if(result== null) throw new AddressNotFoundException("Address with "+ id +" not found");
        // see if can throw in service -- orElseThrow() usage?
        else
            return  new ResponseEntity<>(result, HttpStatus.FOUND);

    }
}
