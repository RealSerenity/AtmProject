package com.rserenity.atmproject.data.repository;

import com.rserenity.atmproject.data.entity.AccountEntity;
import com.rserenity.atmproject.data.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository  extends JpaRepository<RoleEntity,Long> {

    @Query("SELECT t FROM RoleEntity t WHERE t.name = ?1")
    RoleEntity findByName(String role_admin);
}
