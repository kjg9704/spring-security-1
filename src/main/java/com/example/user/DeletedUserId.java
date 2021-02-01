package com.example.user;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Builder;
import lombok.Data;

@Data
@Embeddable
public class DeletedUserId implements Serializable{

	@Column(name = "userEmail")
	 private String userEmail;
	 
	 @Column(name = "logintype")
	 private String logintype;

	 @Builder
	 public DeletedUserId() {
		 
	 }
}
