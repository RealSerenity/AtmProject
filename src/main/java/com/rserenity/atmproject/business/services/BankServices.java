package com.rserenity.atmproject.business.services;

import com.rserenity.atmproject.business.dto.AccountDto;
import com.rserenity.atmproject.business.dto.BankDto;
import com.rserenity.atmproject.business.dto.CustomerDto;
import com.rserenity.atmproject.data.entity.BankEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface BankServices {
    //CRUD
    public List<BankDto> getAllBanks();
    public BankDto createBank(BankDto bankDto);
    public ResponseEntity<BankDto> getBankById(Long id) throws Throwable;

    public BankDto getBankNameById(Long id) throws Throwable;
    public ResponseEntity<BankDto> updateBankById(Long id,BankDto bankDto) throws Throwable;
    public ResponseEntity<Map<String,Boolean>> deleteBank(Long id) throws Throwable;


    public List<AccountDto> getAllBankAccounts(Long id);


    //model mapper
    public BankDto entityToDto(BankEntity bankEntity);
    public BankEntity dtoToEntity(BankDto bankDto);
}
