package com.mywallet.api.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "t_wallet")
@EntityListeners(AuditingEntityListener.class)
@Setter
@Getter
@NoArgsConstructor
public class Wallet implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="id")
	private String id;
	
	@Column(name="backgroundColor")
	private String backgroundColor;
	
	@Column(name="textColor")
	private String textColor = "#FF000000";
	
	@Column(name="name")
	private String name;
	
	@Column(name="nominal")
	private Double nominal;
	
	/**@Column(name="mutasi")
	private ArrayList<String> mutasi;**/
	
	@Column(name="type")
	private String type;

	@JsonIgnore
	@ManyToOne
	private User user;
	
	public Wallet(String id, String backgroundColor, String name, Double nominal, String type) {
		this.id = id;
		this.backgroundColor = backgroundColor;
		this.name = name;
		this.nominal = nominal;
		this.textColor = "#FF000000";
		//this.mutasi = new ArrayList<>();
		this.type = type;
	}
	
	public Wallet(String id, String backgroundColor, String name, Double nominal, String textColor, String type) {
		this.id = id;
		this.backgroundColor = backgroundColor;
		this.name = name;
		this.nominal = nominal;
		this.textColor = textColor;
		//this.mutasi = new ArrayList<>();
		this.type = type;
	}

}
