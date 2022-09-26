package com.rserenity.atmproject.business.services;

import com.rserenity.atmproject.business.dto.AccountDto;
import com.rserenity.atmproject.business.dto.CustomerDto;
import com.rserenity.atmproject.data.entity.CustomerEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface CustomerServices {

    //CRUD
    public List<CustomerDto> getAllCustomers();
    public CustomerDto createCustomer(CustomerDto customerDto);
    public ResponseEntity<CustomerDto> getCustomerById(Long id) throws Throwable;
    public ResponseEntity<CustomerDto> updateCustomerById(Long id,CustomerDto customerDto) throws Throwable;
    public ResponseEntity<CustomerDto> updateCustomerByIdExcPassword(Long id,CustomerDto customerDto) throws Throwable;
    public ResponseEntity<Map<String,Boolean>> deleteCustomer(Long id) throws Throwable;

    public boolean customerLogin(CustomerDto customerDto);
    public CustomerEntity getCustomerByName(String userName);
    public List<AccountDto> getCustomerAllBankAccounts(Long id);

    //model mapper
    public CustomerDto entityToDto(CustomerEntity customerEntity);
    public CustomerEntity dtoToEntity(CustomerDto customerDto);
}
