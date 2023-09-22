package com.example.projectfilrouge.service;

import com.example.projectfilrouge.dto.TicketDto;
import com.example.projectfilrouge.entity.Ticket;
import com.example.projectfilrouge.entity.UserEntity;
import com.example.projectfilrouge.exception.NotAllowedException;
import com.example.projectfilrouge.exception.NotFoundException;
import com.example.projectfilrouge.repository.TicketRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TicketService {

    private TicketRepository ticketRepository;

    public void saveTicket(TicketDto ticketDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = (UserEntity) auth.getPrincipal();

        Ticket ticket = new Ticket();
        ticket.setEventName(ticketDto.getEventName());
        ticket.setEventDate(ticketDto.getEventDate());
        ticket.setPrice(ticketDto.getPrice());
        ticket.setDetails(ticketDto.getDetails());
        ticket.setState(ticketDto.getState());
        ticket.setSeller(userEntity);

        ticketRepository.save(
                ticket
        );
    }

    public List<Ticket> findAllTickets() {
        return ticketRepository.findAll();
    }

    public Ticket findTicketById(Long id) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        if (ticket.isEmpty()) {
            throw new NotFoundException("ticket with id: " + id + " cannot be retrieved");
        }
        return ticket.get();
    }

    public void updateTicketValue(Long id, TicketDto ticketDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = (UserEntity) auth.getPrincipal();

        Ticket ticket = findTicketById(id);

        if (!ticket.getSeller().getId().equals(userEntity.getId())) {
            throw new NotAllowedException();
        }

        ticket.setEventName(ticketDto.getEventName());
        ticket.setEventDate(ticketDto.getEventDate());
        ticket.setPrice(ticketDto.getPrice());
        ticket.setDetails(ticketDto.getDetails());
        ticket.setState(ticketDto.getState());
        ticketRepository.save(
                ticket
        );
    }

    public void deleteTicket(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = (UserEntity) auth.getPrincipal();
        Ticket ticket = findTicketById(id);
        if (!ticket.getSeller().getId().equals(userEntity.getId())) {
            throw new NotAllowedException();
        }
        ticketRepository.deleteById(id);
    }
}
