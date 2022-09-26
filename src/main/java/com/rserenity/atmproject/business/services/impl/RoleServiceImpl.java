package com.rserenity.atmproject.business.services.impl;

import com.rserenity.atmproject.Exception.ResourceNotFoundException;
import com.rserenity.atmproject.business.dto.CustomerDto;
import com.rserenity.atmproject.business.dto.RoleDto;
import com.rserenity.atmproject.business.services.RoleServices;
import com.rserenity.atmproject.data.entity.CustomerEntity;
import com.rserenity.atmproject.data.entity.RoleEntity;
import com.rserenity.atmproject.data.repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.List;
@Service
public class RoleServiceImpl implements RoleServices {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<RoleDto> getAllRoles() {
        List<RoleDto> roleDtos = new ArrayList<>();
        Iterable<RoleEntity> entityList= roleRepository.findAll();
        for(RoleEntity entity : entityList){
            RoleDto roleDto = entityToDto(entity);
            roleDtos.add(roleDto);
        }
        return roleDtos;
    }

    @Override
    public RoleDto createRole(RoleDto roleDto) {
        RoleEntity roleEntity = dtoToEntity(roleDto);
        roleEntity.setName(roleDto.getName());
        roleRepository.save(roleEntity);
        return roleDto;
    }

    @Override
    public ResponseEntity<RoleDto> getRoleById(Long id) throws Throwable {
        RoleEntity roleEntity = roleRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Customer not exist by given id " + id));
        RoleDto roleDto = entityToDto(roleEntity);
        return ResponseEntity.ok(roleDto);
    }

    @Override
    public RoleDto getRoleByName(String name) {
       RoleEntity entity = roleRepository.findByName(name);
       return entityToDto(entity);
    }

    @Override
    public RoleDto entityToDto(RoleEntity roleEntity) {
        RoleDto roleDto = modelMapper.map(roleEntity, RoleDto.class);
        return roleDto;
    }

    @Override
    public RoleEntity dtoToEntity(RoleDto roleDto) {
        RoleEntity roleEntity = modelMapper.map(roleDto, RoleEntity.class);
        return roleEntity;
    }

}
