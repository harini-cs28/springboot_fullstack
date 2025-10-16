package com.example.db_connection.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.db_connection.entity.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}