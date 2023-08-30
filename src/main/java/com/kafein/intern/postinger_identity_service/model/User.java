package com.kafein.intern.postinger_identity_service.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name = "USER_TABLE", uniqueConstraints={@UniqueConstraint(columnNames={"USERNAME"}),@UniqueConstraint(columnNames={"EMAIL"})})
public class User implements UserDetails, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "EMAIL")
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "info_id", referencedColumnName = "id")
    private User_Info info;


    @ManyToOne
    @JoinColumn(name="role_id", nullable=false)
    private Role role;

    //public User() {}

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User otherUser = (User) o;
        return Objects.equals(username, otherUser.username) &&
                Objects.equals(email, otherUser.email) &&
                Objects.equals(password, otherUser.password) &&
                Objects.equals(info, otherUser.info) &&
                Objects.equals(role, otherUser.role);
    }

    public String getPassword() {
        return password;
    }
    public String getUsername() {
        return username;
    }


    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities_ = new ArrayList<>();
        for (Authority authority : role.getAuthorities()) {
            authorities_.add(new SimpleGrantedAuthority(authority.getName()));
        }return authorities_;
    }


    public boolean isAccountNonExpired() {
        return true;
    }
    public boolean isAccountNonLocked() {
        return true;
    }
    public boolean isCredentialsNonExpired() {
        return true;
    }
    public boolean isEnabled() {
        return true;
    }


}
