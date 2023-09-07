package com.example.projectfilrouge.repository;

import com.example.projectfilrouge.entity.Ticket;
import com.example.projectfilrouge.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends   JpaRepository<Ticket, Long>{

    @Override
    Page<Ticket> findAll(Pageable pageable);
}
