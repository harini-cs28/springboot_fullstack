package com.example.db_connection.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.db_connection.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
