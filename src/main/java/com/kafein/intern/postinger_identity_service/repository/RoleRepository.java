package com.kafein.intern.postinger_identity_service.repository;

import com.kafein.intern.postinger_identity_service.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
    Role findByDescription(String description);

}
