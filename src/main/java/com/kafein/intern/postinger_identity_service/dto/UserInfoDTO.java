package com.kafein.intern.postinger_identity_service.dto;

import com.kafein.intern.postinger_identity_service.enums.Gender;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class UserInfoDTO implements Serializable {

    private Long id;
    private String fullname;
    private LocalDate birthDate;
    private Gender gender;
    private long followingCount;
    private long followerCount;

}
