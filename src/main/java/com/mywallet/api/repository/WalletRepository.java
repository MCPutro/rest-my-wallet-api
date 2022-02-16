package com.mywallet.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mywallet.api.entity.User;
import com.mywallet.api.entity.Wallet;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, String> {
	List<Wallet> findByUserOrderByNameAsc(User user);
}
