package com.example.config;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmailAndLogintype(String email, String loginType);
  Boolean existsByEmail(String email);
  void deleteByEmailAndLogintype(String email, String loginType);
  Optional<User> findByEmail(String email);
}