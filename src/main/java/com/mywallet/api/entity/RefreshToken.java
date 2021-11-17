package com.mywallet.api.entity;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_refresh_token")
public class RefreshToken implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "user_id")
	private long id;

	@Column(nullable = false, unique = true)
	private String token;

	@Column(nullable = false)
	private Instant expiryDate;

	@JsonIgnore
	@OneToOne
	@MapsId
	@JoinColumn(name = "user_id")
	private User user;

	
	
}
