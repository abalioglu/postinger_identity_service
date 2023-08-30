package com.kafein.intern.postinger_identity_service.model;

import lombok.Data;

import javax.persistence.*;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name = "ROLE_TABLE", uniqueConstraints={@UniqueConstraint(columnNames={"ROLE_DESCRIPTION"}),@UniqueConstraint(columnNames={"ROLE_NAME"})})
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROLE_ID")
    private Long id;

    //@OneToMany(mappedBy="role")
    //private Set<User> users;

    @Column(name = "ROLE_NAME")
    private String name;

    @Column(name = "ROLE_DESCRIPTION")
    private String description;

    @ManyToMany(fetch = FetchType.EAGER) //eklenti
    @JoinTable(
            name = "role_authority",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    List<Authority> authorities;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Role otherRole = (Role) o;
        return Objects.equals(name, otherRole.name) &&
                Objects.equals(description, otherRole.description) &&
                Objects.equals(authorities, otherRole.authorities);
    }


}
