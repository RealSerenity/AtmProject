package com.rserenity.atmproject.ui.rest;

import com.rserenity.atmproject.Exception.ResourceNotFoundException;
import com.rserenity.atmproject.business.dto.RoleDto;
import com.rserenity.atmproject.business.services.CustomerServices;
import com.rserenity.atmproject.business.services.RoleServices;
import com.rserenity.atmproject.data.entity.RoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "http://localhost:3000")
public class RoleController {

    @Autowired
    RoleServices roleServices;

    @GetMapping("/getAllRoles")
    public List<RoleDto> getAllRoles() {
        List<RoleDto> roleDtos = roleServices.getAllRoles();
        return roleDtos;
    }


    @PostMapping("/createRole")
    public RoleDto createRole(@RequestBody RoleDto roleDto) {
        roleServices.createRole(roleDto);
        return roleDto;
    }

    @GetMapping("/getRole/{id}")
    public ResponseEntity<RoleDto> getRoleById(@PathVariable(name = "id") Long id) throws Throwable {
        return roleServices.getRoleById(id);
    }

    @GetMapping("/getRole/{name}")
    public RoleDto getRoleByName(@PathVariable(value = "name") String name) {
       return roleServices.getRoleByName(name);
    }
}
