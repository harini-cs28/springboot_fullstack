package com.example.db_connection.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.db_connection.entity.Train;

public interface TrainRepository extends JpaRepository<Train, Long> {
}

