package com.rserenity.atmproject.business.services.impl;

import com.rserenity.atmproject.Exception.ResourceNotFoundException;
import com.rserenity.atmproject.business.dto.AccountDto;
import com.rserenity.atmproject.business.dto.CustomerDto;
import com.rserenity.atmproject.business.services.CustomerServices;
import com.rserenity.atmproject.data.entity.CustomerEntity;
import com.rserenity.atmproject.data.repository.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerServices {

    @Autowired
    private CustomerRepository customerRepository;

    //model mapper
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<CustomerDto> getAllCustomers() {
        List<CustomerDto> customerDtoList = new ArrayList<>();
        Iterable<CustomerEntity> entityList= customerRepository.findAll();
        for(CustomerEntity entity : entityList){
            CustomerDto customerDto = entityToDto(entity);
            customerDtoList.add(customerDto);
        }
        return customerDtoList;
    }

    @Override
    public CustomerDto createCustomer(@RequestBody CustomerDto customerDto) {
        CustomerEntity customerEntity = dtoToEntity(customerDto);
        customerEntity.setPassword(passwordEncoder.encode(customerEntity.getPassword()));
        customerRepository.save(customerEntity);
        return customerDto;
    }

    @Override
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable(name = "id") Long id) throws Throwable {
        CustomerEntity customerEntity = customerRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Customer not exist by given id " + id));
        CustomerDto employeeDto = entityToDto(customerEntity);
        return ResponseEntity.ok(employeeDto);
    }

    @Override
    public ResponseEntity<CustomerDto> updateCustomerById(@PathVariable(name = "id") Long id,@RequestBody CustomerDto newCustomerDto) throws Throwable {
        CustomerEntity customerEntity = dtoToEntity(newCustomerDto); // ModelMapper
        CustomerEntity oldCustomer = (CustomerEntity) customerRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Employee not exist by given id " + id));
        oldCustomer.setRoles(customerEntity.getRoles());
        oldCustomer.setUsername(customerEntity.getUsername());
        oldCustomer.setBankAccounts(customerEntity.getBankAccounts());
        oldCustomer.setPassword(passwordEncoder.encode(customerEntity.getPassword()));

        CustomerEntity updatedCustomer = (CustomerEntity) customerRepository.save(oldCustomer);
        CustomerDto customerDto = entityToDto(updatedCustomer);
        return ResponseEntity.ok(customerDto);
    }
    @Override
    public ResponseEntity<CustomerDto> updateCustomerByIdExcPassword(@PathVariable(name = "id") Long id,@RequestBody CustomerDto newCustomerDto) throws Throwable {
        CustomerEntity customerEntity = dtoToEntity(newCustomerDto); // ModelMapper
        CustomerEntity oldCustomer = (CustomerEntity) customerRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Employee not exist by given id " + id));
        oldCustomer.setRoles(customerEntity.getRoles());
        oldCustomer.setUsername(customerEntity.getUsername());
        oldCustomer.setBankAccounts(customerEntity.getBankAccounts());

        CustomerEntity updatedCustomer = (CustomerEntity) customerRepository.save(oldCustomer);
        CustomerDto customerDto = entityToDto(updatedCustomer);
        return ResponseEntity.ok(customerDto);
    }


    @Override
    public ResponseEntity<Map<String, Boolean>> deleteCustomer(@PathVariable(name = "id") Long id) throws Throwable {
        CustomerEntity customerEntity = (CustomerEntity) customerRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Employee not exist by given id " + id));
        customerRepository.delete(customerEntity);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

    @Override
    public boolean customerLogin(@RequestBody CustomerDto customerDto) {
        CustomerEntity entity=  getCustomerByName(customerDto.getUsername());

        if(entity != null &&  passwordEncoder.matches(customerDto.getPassword(),entity.getPassword())){
            //şifreler eşleştirildi
            entity.setLastLogin(new Date());
            customerRepository.save(entity);
            return true;
        }else {
            //şifrede sıkıntı var
            return false;
        }
    }

    @Override
    public CustomerEntity getCustomerByName(@PathVariable(value = "userName") String userName) {
       return customerRepository.findByUserName(userName);
    }

    @Override
    public List<AccountDto> getCustomerAllBankAccounts(@PathVariable(name = "id") Long id) {
        CustomerEntity customerEntity = customerRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Employee not exist by given id " + id));
        return customerEntity.getBankAccounts();
    }

    public void addBankAccountToCustomerById(Long id) throws Throwable {
      ResponseEntity<CustomerDto> customerdto = getCustomerById(id);

    }


    @Override
    public CustomerDto entityToDto(CustomerEntity customerEntity) {
        CustomerDto customerDto = modelMapper.map(customerEntity, CustomerDto.class);
        return customerDto;
    }

    @Override
    public CustomerEntity dtoToEntity(CustomerDto customerDto) {
        CustomerEntity customerEntity = modelMapper.map(customerDto, CustomerEntity.class);
        return customerEntity;
    }
}
