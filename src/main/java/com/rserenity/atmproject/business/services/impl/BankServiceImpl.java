package com.rserenity.atmproject.business.services.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rserenity.atmproject.Exception.ResourceNotFoundException;
import com.rserenity.atmproject.business.dto.AccountDto;
import com.rserenity.atmproject.business.dto.BankDto;
import com.rserenity.atmproject.business.services.BankServices;
import com.rserenity.atmproject.data.entity.BankEntity;
import com.rserenity.atmproject.data.repository.BankRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BankServiceImpl implements BankServices {

    // repository
    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/banks")
    @Override
    public List<BankDto> getAllBanks() {
        List<BankDto> bankDtoList = new ArrayList<>();
        Iterable<BankEntity> entityList= bankRepository.findAll();
        for(BankEntity entity : entityList){
            BankDto bankDto = entityToDto(entity);
            bankDtoList.add(bankDto);
        }
        return bankDtoList;
    }

    @PostMapping("/banks")
    @Override
    public BankDto createBank(@RequestBody BankDto bankDto) {
        BankEntity bankEntity = dtoToEntity(bankDto);
        bankRepository.save(bankEntity);
        return bankDto;
    }

    @GetMapping("/banks/{id}")
    @Override
    public ResponseEntity<BankDto> getBankById(@PathVariable(name = "id") Long id) throws Throwable {
        BankEntity bankEntity = bankRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Employee not exist by given id " + id));
        BankDto bankDto = entityToDto(bankEntity);
        return ResponseEntity.ok(bankDto);
    }
    @GetMapping("/banks/{id}")
    @Override
    public BankDto getBankNameById(@PathVariable(name = "id") Long id) throws Throwable {
        BankEntity bankEntity = bankRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Employee not exist by given id " + id));
        return entityToDto(bankEntity);
    }


    @PutMapping("/banks/{id}")
    @Override
    public ResponseEntity<BankDto> updateBankById(@PathVariable(name = "id") Long id,@RequestBody BankDto bankNew) throws Throwable {
        BankEntity newBankEntity = dtoToEntity(bankNew); // ModelMapper
        BankEntity oldBankEntity = (BankEntity) bankRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Employee not exist by given id " + id));

        oldBankEntity.setBankName(newBankEntity.getBankName());
        oldBankEntity.setBankAccounts(newBankEntity.getBankAccounts());

        BankEntity updatedEmployee = (BankEntity) bankRepository.save(oldBankEntity);
        BankDto bankDto = entityToDto(updatedEmployee);
        return ResponseEntity.ok(bankDto);
    }
    @DeleteMapping("/banks/{id}")
    @Override
    public ResponseEntity<Map<String, Boolean>> deleteBank(@PathVariable(name = "id") Long id) throws Throwable {
        BankEntity employeeEntity = (BankEntity) bankRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Employee not exist by given id " + id));
        bankRepository.delete(employeeEntity);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/banks/getAccounts/{id}")
    @Override
    public List<AccountDto> getAllBankAccounts(@PathVariable(name = "id") Long id) {
        BankEntity bankEntity = bankRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Employee not exist by given id " + id));
        return bankEntity.getBankAccounts();
    }

    @Override
    public BankDto entityToDto(BankEntity bankEntity) {
        BankDto bankDto = modelMapper.map(bankEntity, BankDto.class);
        return bankDto;
    }

    @Override
    public BankEntity dtoToEntity(BankDto bankDto) {
        BankEntity bankEntity = modelMapper.map(bankDto, BankEntity.class);
        return bankEntity;
    }
}
