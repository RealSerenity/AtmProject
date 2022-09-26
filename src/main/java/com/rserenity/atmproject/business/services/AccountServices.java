package com.rserenity.atmproject.business.services;

import com.rserenity.atmproject.business.dto.AccountDto;
import com.rserenity.atmproject.data.entity.AccountEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface AccountServices {

    //CRUD
    public List<AccountDto> getAllAccounts();
    public AccountDto createAccount(AccountDto accountDto);
    public ResponseEntity<AccountDto> getAccountById(Long id) throws Throwable;
    public ResponseEntity<AccountDto> updateAccountById(Long id,AccountDto AccountDto) throws Throwable;
    public ResponseEntity<Map<String,Boolean>> deleteAccount(Long id) throws Throwable;

    //model mapper
    public AccountDto entityToDto(AccountEntity accountEntity);
    public AccountEntity dtoToEntity(AccountDto accountDto);

    List<AccountDto> getCustomerAccountsByCustomerId(Long id);
}
