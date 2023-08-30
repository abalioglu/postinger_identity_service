package com.kafein.intern.postinger_identity_service.repository;
import com.kafein.intern.postinger_identity_service.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findByName(String name);
    Authority findByDescription(String description);
}
