package com.kafein.intern.postinger_identity_service.dto;

import com.kafein.intern.postinger_identity_service.model.Role;
import com.kafein.intern.postinger_identity_service.model.User_Info;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserDTO implements Serializable {

    private Long id;
    private String username;
    private String password;
    private String email;
    private UserInfoDTO info;
    private RoleDTO role;
}
