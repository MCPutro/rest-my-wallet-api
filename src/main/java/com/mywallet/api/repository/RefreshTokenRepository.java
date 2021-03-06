package com.mywallet.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.mywallet.api.entity.RefreshToken;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	
	Optional<RefreshToken> findByToken (String token);
	
	@Modifying
	@Query(value = "DELETE FROM public.t_refresh_token o WHERE o.token= ?1", nativeQuery= true)
	int deleteByToken(String token);
	
}
