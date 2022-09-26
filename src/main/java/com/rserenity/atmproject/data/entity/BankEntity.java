package com.rserenity.atmproject.data.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@Log4j2
@Builder
@Entity
@Table(name = "banks")
public class BankEntity extends BaseEntity{

    @Column(name = "bank_name")
    private String bankName;

    @OneToMany(targetEntity= AccountEntity.class)
    @Column(name = "bank_accounts")
    private List bankAccounts;

    public BankEntity(String bankName) {
        this.bankName = bankName;
    }

    public BankEntity(String bankName, List bankAccounts) {
        this.bankName = bankName;
        this.bankAccounts = bankAccounts;
    }
}
