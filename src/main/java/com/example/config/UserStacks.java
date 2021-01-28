package com.example.config;

import javax.persistence.Entity;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class UserStacks {
	
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
    
    @Column(nullable = false)
	private String stack;
	
	@ManyToOne
    @JoinColumn(name = "userindex", nullable = false, updatable = false)
    private User user;
	
	@Builder
	public UserStacks(String stack, User user) {
		this.stack = stack;
		this.user = user;
	}
}
