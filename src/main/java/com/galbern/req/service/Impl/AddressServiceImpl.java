package com.galbern.req.service.Impl;

import com.galbern.req.dao.AddressDao;
import com.galbern.req.domain.Address;
import com.galbern.req.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    public AddressDao addressDao;


    @Override
    public Address addAddress(Address a) {

        return addressDao.save( a);
    }

    @Override
    public Set<Address> getAddresses() {
        Set<Address> result = new HashSet<>((Collection<? extends Address>) addressDao.findAll());
        return result;
    }

    @Override
    public Address findAddressById(Long id) {
        return addressDao.findById(id).orElse(null); // AddressNotFoundException::new
    }


}
