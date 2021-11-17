package com.mywallet.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mywallet.api.entity.User;
import com.mywallet.api.entity.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, String> {
	List<Wallet> findByUser(User user);
}
