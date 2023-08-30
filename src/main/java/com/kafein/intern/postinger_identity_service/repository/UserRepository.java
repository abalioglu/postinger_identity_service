package com.kafein.intern.postinger_identity_service.repository;
import com.kafein.intern.postinger_identity_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByRole_Id(Long roleId);
    User findByUsername(String username);
    User findByEmail(String email);
    List<User> findAllByInfo_Fullname(String fullname);
}


