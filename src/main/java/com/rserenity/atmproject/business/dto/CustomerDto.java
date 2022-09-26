package com.rserenity.atmproject.business.dto;

import com.rserenity.atmproject.data.entity.RoleEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Log4j2
@Builder
public class CustomerDto implements Serializable {

    private Long id;

    @NotEmpty(message = "Kullanıcı adı alanı boş bırakılamaz")
    @Size(min = 5,max = 50, message = "Kullanıcı adı uzunluğu '5' ile '50' arasında olmalıdır")
    private String username;

    @NotEmpty(message = "Şifre alanı boş bırakılamaz")
    @Size(min = 6,max = 20,message = "Şifre uzunluğu '6' ile 20 arasında olmalıdır")
    private String password;

    private Collection<RoleDto> roles;

    private Date lastLogin;
}
