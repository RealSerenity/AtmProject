package com.rserenity.atmproject.data.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.util.Collection;

@Data
@NoArgsConstructor
@Log4j2
@Builder
@Entity
@Table(name = "accounts")
public class AccountEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "customerId")
    private Long customerId;

    @Column(name = "bankId")
    private Long bankId;

    @Column(name = "money")
    private Long money;

    public AccountEntity(Long customerId, Long bankId, Long money) {
        this.customerId = customerId;
        this.bankId = bankId;
        this.money = money;
    }
}
