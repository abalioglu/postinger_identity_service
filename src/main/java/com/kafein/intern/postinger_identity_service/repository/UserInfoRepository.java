package com.kafein.intern.postinger_identity_service.repository;

import com.kafein.intern.postinger_identity_service.model.User_Info;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<User_Info, Long> {


}