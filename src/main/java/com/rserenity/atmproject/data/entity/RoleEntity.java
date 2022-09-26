package com.rserenity.atmproject.data.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.persistence.*;
import java.util.Collection;
@Data
@NoArgsConstructor
@Log4j2
@Builder
@Entity
@Table(name = "roles")
public class RoleEntity extends BaseEntity{

    private String name;

   // @ManyToMany(mappedBy = "roles")
   // private Collection<CustomerEntity> users;

    public RoleEntity(String name) {
        this.name = name;
    }
}
