package com.example.projectfilrouge.controller;

import com.example.projectfilrouge.dto.TicketDto;
import com.example.projectfilrouge.dto.TicketFilterDto;
import com.example.projectfilrouge.entity.Ticket;
import com.example.projectfilrouge.service.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
    public List<Ticket> getAllTickets(
            @PathVariable String eventName,
            @PathVariable Date eventStartDate,
            @PathVariable Date eventEndDate,
            @PathVariable Double priceMin,
            @PathVariable Double priceMax,
            @PathVariable String details,
            @PathVariable String state,
            @PathVariable List<String> tags
    ) {
        return ticketService.findAllTickets(eventName, eventStartDate, eventEndDate, priceMin, priceMax, details, state, tags);
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

    @PutMapping("/{id}/buy")
    public void buyTicket(@PathVariable Long id) {
        ticketService.buyTicket(id);
    }

    @PutMapping("/{id}/rate")
    public void rateTransaction(@PathVariable Long id, int rating) {
        ticketService.rateTransaction(id, rating);
    }
}
