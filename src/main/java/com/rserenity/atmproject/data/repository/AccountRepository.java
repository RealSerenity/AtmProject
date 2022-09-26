package com.rserenity.atmproject.data.repository;

import com.rserenity.atmproject.data.entity.AccountEntity;
import com.rserenity.atmproject.data.entity.BankEntity;
import com.rserenity.atmproject.data.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity,Long> {

    @Query("SELECT t FROM AccountEntity t WHERE t.customerId = ?1")
    AccountEntity getAccountsByCustomerId(Long id);

}
