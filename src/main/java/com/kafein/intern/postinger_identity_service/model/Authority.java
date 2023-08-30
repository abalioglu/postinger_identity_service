package com.kafein.intern.postinger_identity_service.model;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
@Data
@Entity
@Table(name = "AUTHORITIES_TABLE", uniqueConstraints={@UniqueConstraint(columnNames={"AUTHORITY_DESCRIPTION"}),@UniqueConstraint(columnNames={"AUTHORITY_NAME"})})
public class Authority implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AUTHORITY_ID")
    private Long id;

    @Column(name = "AUTHORITY_NAME")
    private String name;

    @Column(name = "AUTHORITY_DESCRIPTION")
    private String description;

   // @ManyToMany(mappedBy = "authorities")
   // private Set<Role> roles;



    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Authority otherRole = (Authority) o;
        return Objects.equals(name, otherRole.name) &&
                Objects.equals(description, otherRole.description);
    }


}
