package com.rserenity.atmproject.ui.rest;

import com.rserenity.atmproject.Exception.ResourceNotFoundException;
import com.rserenity.atmproject.business.dto.AccountDto;
import com.rserenity.atmproject.business.dto.CustomerDto;
import com.rserenity.atmproject.business.dto.RoleDto;
import com.rserenity.atmproject.business.services.CustomerServices;
import com.rserenity.atmproject.business.services.RoleServices;
import com.rserenity.atmproject.data.entity.CustomerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "http://localhost:3000")
public class CustomerController {

    @Autowired
    CustomerServices customerServices;

    @Autowired
    RoleServices roleServices;

    // ROOT
    // http://localhost:8080/api/v1/index
    @GetMapping({"/index","/"})
    public String getRoot(){
        return "index";
    }

    // LIST
    // http://localhost:8080/api/v1/employees
    @GetMapping("/getAllCustomers")
    public List<CustomerDto> getAllCustomers(){
        List<CustomerDto> customerDtoList =  customerServices.getAllCustomers();
        return customerDtoList;
    }

    // FIND /////////////////////
    @GetMapping("/getCustomer/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable Long id) throws Throwable {
        ResponseEntity<CustomerDto> customerById = customerServices.getCustomerById(id);
        return customerById;
    }

    @PostMapping("/createCustomer")
    public CustomerDto createCustomer(@RequestBody CustomerDto customerDto){
        customerServices.createCustomer(customerDto);
        return customerDto;
    }

    @PutMapping("/updateCustomer/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable(name = "id") Long id, @RequestBody CustomerDto customerDto) throws Throwable {
        //return employeeServices.updateEmployeeById(id, employeeDto);
        customerServices.updateCustomerByIdExcPassword(id, customerDto);
        return ResponseEntity.ok(customerDto);
    }

    @DeleteMapping("/deleteCustomer/{id}")
    public ResponseEntity<Map<String,Boolean>> deleteCustomer(@PathVariable(name = "id") Long id) throws Throwable {
        customerServices.deleteCustomer(id);
        Map<String,Boolean> response = new HashMap<>();
        response.put("deleted",Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getCustomerAccounts/{id}")
    public List<AccountDto> getCustomerAllBankAccounts(@PathVariable(name = "id") Long id) {
        List<AccountDto>  accountDtoList = customerServices.getCustomerAllBankAccounts(id);
        return accountDtoList;
    }

    @PostMapping("/customers/customerLogin")
    public boolean customerLogin(@RequestBody CustomerDto customerDto) {
        return customerServices.customerLogin(customerDto);
    }



    @GetMapping("/customers/{username}")
    public CustomerEntity getCustomerByName(@PathVariable(value = "username") String userName) {
        return customerServices.getCustomerByName(userName);
    }

    @PutMapping("/updateCustomer/{id}/{roleId}")
    public ResponseEntity<CustomerDto> addRoleToCustomer(@PathVariable(name = "id") Long id, @PathVariable(name = "roleId") Long roleId ) throws Throwable {
        //return employeeServices.updateEmployeeById(id, employeeDto);
        CustomerDto customerDto = getCustomerById(id).getBody();
        RoleDto role = roleServices.getRoleById(roleId).getBody();
        Collection<RoleDto> roles = customerDto.getRoles();
        roles.add(role);
        customerDto.setRoles(roles);
        customerServices.updateCustomerByIdExcPassword(id, customerDto);
        return ResponseEntity.ok(customerDto);
    }

}
