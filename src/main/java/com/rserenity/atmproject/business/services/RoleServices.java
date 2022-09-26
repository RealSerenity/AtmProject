package com.rserenity.atmproject.business.services;

import com.rserenity.atmproject.business.dto.AccountDto;
import com.rserenity.atmproject.business.dto.RoleDto;
import com.rserenity.atmproject.data.entity.AccountEntity;
import com.rserenity.atmproject.data.entity.RoleEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RoleServices {

    public List<RoleDto> getAllRoles();

    public RoleDto createRole(RoleDto roleDto);

    public ResponseEntity<RoleDto> getRoleById(Long id) throws Throwable;

    public RoleDto entityToDto(RoleEntity roleEntity);
    public RoleEntity dtoToEntity(RoleDto roleDto);

    public RoleDto getRoleByName(String name);
}
