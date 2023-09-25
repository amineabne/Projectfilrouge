package com.example.projectfilrouge.controller;

import com.example.projectfilrouge.dto.TicketDto;
import com.example.projectfilrouge.dto.TicketFilterDto;
import com.example.projectfilrouge.entity.Ticket;
import com.example.projectfilrouge.service.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
            @RequestParam Optional<String> eventName,
            @RequestParam Optional<Date> eventStartDate,
            @RequestParam Optional<Date> eventEndDate,
            @RequestParam Optional<Double> priceMin,
            @RequestParam Optional<Double> priceMax,
            @RequestParam Optional<String> details,
            @RequestParam Optional<String> state,
            @RequestParam Optional<List<String>> tags
    ) {
        return ticketService.findAllTickets(eventName.orElse(null), eventStartDate.orElse(null), eventEndDate.orElse(null), priceMin.orElse(null), priceMax.orElse(null), details.orElse(null), state.orElse(null), tags.orElse(null));
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
