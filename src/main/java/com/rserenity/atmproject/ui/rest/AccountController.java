package com.rserenity.atmproject.ui.rest;

import com.rserenity.atmproject.business.dto.AccountDto;
import com.rserenity.atmproject.business.services.AccountServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "http://localhost:3000")
public class AccountController {

    @Autowired
    AccountServices accountServices;


    // ROOT
    // http://localhost:8080/api/v1/index
    @GetMapping({"/index","/"})
    public String getRoot(){
        return "index";
    }

    // LIST
    // http://localhost:8080/api/v1/employees
    @GetMapping("/getAllAccounts")
    public List<AccountDto> getAllAccounts(){
        List<AccountDto> accountDtoList =  accountServices.getAllAccounts();
        return accountDtoList;
    }

    // FIND /////////////////////
    @GetMapping("/getAccount/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable Long id) throws Throwable {
        ResponseEntity<AccountDto> accountById = accountServices.getAccountById(id);
        return accountById;
    }

    @PostMapping("/createAccount")
    public AccountDto createAccount(@RequestBody AccountDto accountDto){
        accountServices.createAccount(accountDto);
        return accountDto;
    }

    @PutMapping("/updateAccount/{id}")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable(name = "id") Long id, @RequestBody AccountDto accountDto) throws Throwable {
        //return employeeServices.updateEmployeeById(id, employeeDto);
        accountServices.updateAccountById(id, accountDto);
        return ResponseEntity.ok(accountDto);
    }

    @DeleteMapping("/deleteAccount/{id}")
    public ResponseEntity<Map<String,Boolean>> deleteBank(@PathVariable(name = "id") Long id) throws Throwable {
        accountServices.deleteAccount(id);
        Map<String,Boolean> response = new HashMap<>();
        response.put("deleted",Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getCustomerAccounts/{id}")
    public List<AccountDto> getCustomerAccountsByCustomerId(Long id) {
        return accountServices.getCustomerAccountsByCustomerId(id);
    }

}
