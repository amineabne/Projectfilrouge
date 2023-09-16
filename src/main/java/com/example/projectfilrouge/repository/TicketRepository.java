package com.example.projectfilrouge.repository;

import com.example.projectfilrouge.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Override
    Page<Ticket> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM TICKET WHERE seller_id = ?1", nativeQuery = true)
    List<Ticket> findTicketOnSale(Long userId);

    @Query(value = "SELECT * FROM TICKET WHERE buyer_id = ?1", nativeQuery = true)
    List<Ticket> findTicketPurchased(Long userId);
}
