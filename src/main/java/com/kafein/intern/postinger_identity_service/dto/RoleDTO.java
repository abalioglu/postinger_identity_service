package com.kafein.intern.postinger_identity_service.dto;
import com.kafein.intern.postinger_identity_service.model.Authority;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RoleDTO implements Serializable {

    private Long id;
    private String name;
    private String description;
    private List<AuthorityDTO> authorities;

}
