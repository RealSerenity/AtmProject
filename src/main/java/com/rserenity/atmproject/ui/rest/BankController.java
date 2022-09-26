package com.rserenity.atmproject.ui.rest;

import com.rserenity.atmproject.business.dto.AccountDto;
import com.rserenity.atmproject.business.dto.BankDto;
import com.rserenity.atmproject.business.dto.CustomerDto;
import com.rserenity.atmproject.business.services.BankServices;
import com.rserenity.atmproject.business.services.CustomerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/banks")
@CrossOrigin(origins = "http://localhost:3000")
public class BankController {

    @Autowired
    BankServices bankServices;

    // ROOT
    // http://localhost:8080/api/v1/index
    @GetMapping({"/index","/"})
    public String getRoot(){
        return "index";
    }

    // LIST
    // http://localhost:8080/api/v1/employees
    @GetMapping("/getAllBanks")
    public List<BankDto> getAllBanks(){
        List<BankDto> bankDtoList =  bankServices.getAllBanks();
        return bankDtoList;
    }

    // FIND /////////////////////
    @GetMapping("/getBank/{id}")
    public ResponseEntity<BankDto> getBankById(@PathVariable Long id) throws Throwable {
        ResponseEntity<BankDto> bankById = bankServices.getBankById(id);
        return bankById;
    }
    @GetMapping("/getBankName/{id}")
    public String getBankNameById(@PathVariable Long id) throws Throwable {
        BankDto bankById = bankServices.getBankNameById(id);
        return bankById.getBankName();
    }

    @PostMapping("/createBank")
    public BankDto createBank(@RequestBody BankDto bankDto){
        bankServices.createBank(bankDto);
        return bankDto;
    }

    @PutMapping("/updateBank/{id}")
    public ResponseEntity<BankDto> updateBank(@PathVariable(name = "id") Long id, @RequestBody BankDto bankDto) throws Throwable {
        //return employeeServices.updateEmployeeById(id, employeeDto);
        bankServices.updateBankById(id, bankDto);
        return ResponseEntity.ok(bankDto);
    }

    @DeleteMapping("/deleteBank/{id}")
    public ResponseEntity<Map<String,Boolean>> deleteBank(@PathVariable(name = "id") Long id) throws Throwable {
        bankServices.deleteBank(id);
        Map<String,Boolean> response = new HashMap<>();
        response.put("deleted",Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getBankAccounts/{id}")
    public List<AccountDto> getAllBankAccounts(@PathVariable(name = "id") Long id) {
        List<AccountDto> accountDtoList = bankServices.getAllBankAccounts(id);
        return accountDtoList;
    }
}
