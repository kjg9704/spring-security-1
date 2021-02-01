package com.example.user;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class DeletedUser  {
	
	@EmbeddedId
	private DeletedUserId deletedUserId = new DeletedUserId();
	
	@Builder
	public DeletedUser(String userEmail, String loginType) {
		deletedUserId.setUserEmail(userEmail);
		deletedUserId.setLogintype(loginType);
	}
}
