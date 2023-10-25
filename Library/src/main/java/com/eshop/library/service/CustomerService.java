package com.eshop.library.service;

import com.eshop.library.dto.CustomerDto;
import com.eshop.library.model.Customer;


public interface CustomerService {
    Customer save(CustomerDto customerDto);

    Customer findByUsername(String username);

    Customer update(CustomerDto customerDto);

    Customer changePass(CustomerDto customerDto);

    CustomerDto getCustomer(String username);
}