package com.kafein.intern.postinger_identity_service.model;
import com.kafein.intern.postinger_identity_service.enums.Gender;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
@Data
@Entity
@Table(name = "USER_INFO_TABLE")
public class User_Info implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "FULLNAME")
    private String fullname;

    @Column(name = "BIO")
    private String bio;

    @Column(name = "BIRTHDATE")
    private LocalDate birthDate;

    @Column(name = "GENDER")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "FOLLOWING_COUNT") //followed
    private long followingCount;

    @Column(name = "FOLLOWER_COUNT")
    private long followerCount;

    //@OneToOne(mappedBy = "info")
    //private User user;

}
