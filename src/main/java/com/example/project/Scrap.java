package com.example.project;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Scrap {
	@EmbeddedId
	private ScrapId scrapId = new ScrapId();
	@Builder
	public Scrap(int postIndex, long userIndex) {
		scrapId.setPostIndex(postIndex);
		scrapId.setUserIndex(userIndex);
	}
}