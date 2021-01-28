package com.example.config;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StackRepository extends JpaRepository<UserStacks, Long> {
	//UserStacks save(List<StackDto> list, User user);
}
