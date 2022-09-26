package com.rserenity.atmproject.data.repository;

import com.rserenity.atmproject.data.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity,Long> {

    @Query("SELECT t FROM CustomerEntity t WHERE t.username = ?1")
    CustomerEntity findByUserName(String userName);

}
