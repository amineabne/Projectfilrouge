package com.example.projectfilrouge.repository;

import com.example.projectfilrouge.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Override
    Page<Ticket> findAll(Pageable pageable);

    @Query(value = """
    SELECT * FROM TICKET 
    LEFT JOIN ticket_tags tt ON id = tt.ticket_id
    WHERE (?1 IS NULL OR LOWER(event_name) like LOWER(?1))
    AND (cast(?2 as date) IS NULL OR event_date > ?2)
    AND (cast(?3 as date) IS NULL OR event_date < ?3)
    AND (?4 IS NULL OR price > ?4)
    AND (?5 IS NULL OR price < ?5)
    AND (?6 IS NULL OR LOWER(details) like LOWER(?6))
    """, nativeQuery = true)
    List<Ticket> findAll(String eventName,
                         Date eventStartDate,
                         Date eventEndDate,
                         Double priceMin,
                         Double priceMax,
                         String details,
                         String state
    );

    @Query(value = "SELECT * FROM TICKET WHERE seller_id = ?1", nativeQuery = true)
    List<Ticket> findTicketOnSale(Long userId);

    @Query(value = "SELECT * FROM TICKET WHERE buyer_id = ?1", nativeQuery = true)
    List<Ticket> findTicketPurchased(Long userId);
}
