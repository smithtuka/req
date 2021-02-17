package com.galbern.req.service;

import com.galbern.req.jpa.entities.Address;

import java.util.List;
import java.util.Set;


public interface AddressService {
    Address addAddress(Address a);
    Set<Address> getAddresses(List<Long> addressList);
    Address findAddressById(Long id);

}
