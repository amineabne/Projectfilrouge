package com.example.projectfilrouge.controller;

import com.example.projectfilrouge.dto.TicketDto;
import com.example.projectfilrouge.entity.Ticket;
import com.example.projectfilrouge.service.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/tickets")
public class TicketController {

    private TicketService ticketService;

    @PostMapping
    public void saveTicket(@RequestBody TicketDto ticketDto) {
        ticketService.saveTicket(ticketDto);
    }

    @GetMapping
    public List<Ticket> getAllTickets() {
       return ticketService.findAllTickets();
    }

    @GetMapping("/{id}")
    public Ticket getTicketById(@PathVariable Long id) {
        return ticketService.findTicketById(id);
    }

    @PutMapping("/{id}")
    public void updateTicketValue(@PathVariable Long id, @RequestBody TicketDto ticketDto) {
        ticketService.updateTicketValue(id, ticketDto);
    }

    @DeleteMapping("/{id}")
    public void deleteTicketById(@PathVariable Long id) {
        ticketService.deleteTicket(id);
    }
}
