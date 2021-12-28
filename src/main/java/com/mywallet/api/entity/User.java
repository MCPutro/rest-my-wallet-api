package com.mywallet.api.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "t_user")
@EntityListeners(AuditingEntityListener.class)
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(unique = true, name = "email")
	private String email;

	@Column(name = "password")
	private String password;

	@Column(name = "username")
	private String username;

	@Column(unique = true, name = "uid")
	private String uid;

	@Column(name = "registrationDate")
	private LocalDateTime registrationDate;
	
	@Column(name = "urlAvatar")
	private String urlAvatar;

	@Column(name = "deviceId")
	private String deviceId;

	@OneToMany(targetEntity = Wallet.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private List<Wallet> wallets;
	
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private RefreshToken refreshToken;

	public User(String email, String password, String username, String uid, String urlAvatar) {
		super();
		this.email = email;
		this.password = password;
		this.username = username;
		this.uid = uid;
		this.urlAvatar = urlAvatar;
	}

}
