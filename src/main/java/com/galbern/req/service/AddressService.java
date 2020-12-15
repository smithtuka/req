package com.galbern.req.service;

import com.galbern.req.domain.Address;

import java.util.Set;


public interface AddressService {
    Address addAddress(Address a);
    Set<Address> getAddresses();
    Address findAddressById(Long id);

}
