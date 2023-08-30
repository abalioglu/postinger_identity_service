package com.kafein.intern.postinger_identity_service.dto;
import lombok.Data;

import java.io.Serializable;

@Data
public class AuthorityDTO implements Serializable {

    private Long id;
    private String name;
    private String description;
}


