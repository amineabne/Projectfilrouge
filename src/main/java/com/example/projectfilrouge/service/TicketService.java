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
//Un override te permet de réécrire sur une fonction présente dans la classe mere (par defaut tu extends
//toujours de la mere des meres objet, meme si y a pas de extend dans ta classe)
@Service
@AllArgsConstructor
public class TicketService {

    private TicketRepository ticketRepository;

    public void saveTicket(TicketDto ticketDto) {
        // on recupere l'identite de la personne connecte a l'aide de ces deux lignes
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //voir cahier pourquoi c'est possible, il recupere un objet de type objet
        UserEntity userEntity = (UserEntity) auth.getPrincipal();

        // on creer le ticket
        Ticket ticket = new Ticket();
        ticket.setEventName(ticketDto.getEventName());
        ticket.setEventDate(ticketDto.getEventDate());
        ticket.setPrice(ticketDto.getPrice());
        ticket.setDetails(ticketDto.getDetails());
        ticket.setState(ticketDto.getState());
        ticket.setSeller(userEntity);

        // on le sauvegarde en DB grâce à l'interface qui extend de jpa et donc la savegarde est implémenter directement
        ticketRepository.save(
                ticket
        );
    }

//cherche l'enseble des tickets grâce au repo qui est interagit avec bdd
    public List<Ticket> findAllTickets() {
        return ticketRepository.findAll();
    }
//Cherche un ticket par son id
    public Ticket findTicketById(Long id) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        //Si l'id du ticket n'est pas présent en bdd alors casse toi
        if(ticket.isEmpty()) {
            throw new NotFoundException("ticket with id: " + id + " cannot be retrieved");
        }
        return ticket.get();
    }

    public void updateTicketValue(Long id, TicketDto ticketDto) {
        // on recupere l'identite de la personne connecte a l'aide de ces deux lignes
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = (UserEntity) auth.getPrincipal();

        // on recupere le ticket
        Ticket ticket = findTicketById(id);

        //On verifie si l'owner du ticket est bien celui qui essaie de le modifier
        if(!ticket.getSeller().getId().equals(userEntity.getId())) {
            //NotAllowedException est une classe qui hérite de RunTimeException pour
            // cibler la cause de l'erreur puisque c'est nous qui l'avons mis en place avec un nom specifique
            // alors que le runtime ayant un nom generique n'aurais pas aider a l'identification de l'erreur
            // (pareil pour le NotFoundException)
            throw new NotAllowedException();
        }
        //Permet de modifier les paramètres
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
        // on recupere l'identite de la personne connecte a l'aide de ces deux lignes
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = (UserEntity) auth.getPrincipal();
        // on recupere le ticket
        Ticket ticket = findTicketById(id);
        //On verifie si l'owner du ticket est bien celui qui essaie de le SUPPRIMER
        if(!ticket.getSeller().getId().equals(userEntity.getId())) {
            throw new NotAllowedException();
        }
        //Poubelle
        ticketRepository.deleteById(id);
    }
}
