package com.mywallet.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mywallet.api.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);
	
	User findByUid(String uid);
}
