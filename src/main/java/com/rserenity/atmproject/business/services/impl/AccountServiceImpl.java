package com.rserenity.atmproject.business.services.impl;

import com.rserenity.atmproject.Exception.ResourceNotFoundException;
import com.rserenity.atmproject.business.dto.AccountDto;
import com.rserenity.atmproject.business.dto.BankDto;
import com.rserenity.atmproject.business.services.AccountServices;
import com.rserenity.atmproject.data.entity.AccountEntity;
import com.rserenity.atmproject.data.repository.AccountRepository;
import org.aspectj.weaver.ast.Literal;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Service
public class AccountServiceImpl implements AccountServices{

    @Autowired
   private AccountRepository accountRepository;

    @Autowired
   private ModelMapper modelMapper;

    @GetMapping("/accounts")
    @Override
    public List<AccountDto> getAllAccounts() {
        List<AccountDto> accountDtoList = new ArrayList<>();
        Iterable<AccountEntity> entityList= accountRepository.findAll();
        for(AccountEntity entity : entityList){
            AccountDto employeeDto = entityToDto(entity);
            accountDtoList.add(employeeDto);
        }
        return accountDtoList;
    }

    @PostMapping("/accounts")
    @Override
    public AccountDto createAccount(@RequestBody AccountDto accountDto) {
        AccountEntity employeeEntity = dtoToEntity(accountDto);
        accountRepository.save(employeeEntity);
        return accountDto;
    }
    @GetMapping("/accounts/{id}")
    @Override
    public ResponseEntity<AccountDto> getAccountById(@PathVariable(name = "id") Long id) throws Throwable {
        AccountEntity accountEntity = accountRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Account not exist by given id " + id));
        AccountDto employeeDto = entityToDto(accountEntity);
        return ResponseEntity.ok(employeeDto);
    }
    @PutMapping("/accounts/{id}")
    @Override
    public ResponseEntity<AccountDto> updateAccountById(@PathVariable(name = "id") Long id,@RequestBody  AccountDto accountDto) throws Throwable {
        AccountEntity employeeEntity = dtoToEntity(accountDto); // ModelMapper
        AccountEntity account = accountRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Employee not exist by given id " + id));
        account.setBankId(employeeEntity.getBankId());
        account.setCustomerId(employeeEntity.getCustomerId());
        account.setMoney(employeeEntity.getMoney());

        AccountEntity updatedAccount = (AccountEntity) accountRepository.save(account);
        AccountDto accountDtoNew = entityToDto(updatedAccount);
        return ResponseEntity.ok(accountDtoNew);
    }
    @DeleteMapping("/accounts/{id}")
    @Override
    public ResponseEntity<Map<String, Boolean>> deleteAccount(Long id) throws Throwable {
        AccountEntity employeeEntity = accountRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Employee not exist by given id " + id));
        accountRepository.delete(employeeEntity);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
    @Override
    public List<AccountDto> getCustomerAccountsByCustomerId(Long id) {
        List<AccountDto> entityList = getAllAccounts();
        List<AccountDto> accounts = new ArrayList<>();
        for(AccountDto entity: entityList){
            if(Objects.equals(entity.getCustomerId(), id)){
                accounts.add(entity);
            }
        }
        return accounts;
    }

    @Override
    public AccountDto entityToDto(AccountEntity accountEntity) {
        AccountDto accountDto = modelMapper.map(accountEntity, AccountDto.class);
        return accountDto;
    }

    @Override
    public AccountEntity dtoToEntity(AccountDto accountDto) {
        AccountEntity accountEntity = modelMapper.map(accountDto, AccountEntity.class);
        return accountEntity;
    }


}
