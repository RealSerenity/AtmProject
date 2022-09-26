package com.rserenity.atmproject.business.dto;

import lombok.*;
import lombok.extern.log4j.Log4j2;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Log4j2
@Builder
public class AccountDto implements Serializable {

    private Long id;

    private Long bankId;

    private Long customerId;

    private Long money;
}
