package com.example.user;

import org.springframework.data.jpa.repository.JpaRepository;


public interface DeletedUserRepository extends JpaRepository<DeletedUser, DeletedUserId> {

}
