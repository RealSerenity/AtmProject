package com.rserenity.atmproject.business.dto;

import com.rserenity.atmproject.data.entity.CustomerEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Log4j2
@Builder
public class RoleDto {

    private Long id;

    private String name;
}
