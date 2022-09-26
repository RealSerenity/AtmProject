package com.rserenity.atmproject.data.entity;

import com.rserenity.atmproject.business.dto.AccountDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Log4j2
@Builder
@Entity
@Table(name = "customers")
public class CustomerEntity extends BaseEntity{

    @Column(name = "user_name",nullable = false, unique = true)
    private String username;

    @Column(name = "password",nullable = false)
    private String password;

    @OneToMany(targetEntity= AccountEntity.class)
    @Column(name = "bankAccounts")
    private List<AccountDto> bankAccounts;

    @Column(name = "last_login")
    private Date lastLogin;

    @ManyToMany
//    @JoinTable(
//            name = "users_roles",
//            joinColumns = @JoinColumn(
//                    name = "user_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(
//                    name = "role_id", referencedColumnName = "id"))
    private Collection<RoleEntity> roles;

    public CustomerEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public CustomerEntity(String username, String password, List<AccountDto> bankAccounts,Collection<RoleEntity> roles) {
        this.username = username;
        this.password = password;
        this.bankAccounts = bankAccounts;
        this.roles = roles;
    }
    public CustomerEntity(String username, String password, List<AccountDto> bankAccounts,Collection<RoleEntity> roles, Date lastLogin) {
        this.username = username;
        this.password = password;
        this.bankAccounts = bankAccounts;
        this.roles = roles;
        this.lastLogin = lastLogin;
    }
}
