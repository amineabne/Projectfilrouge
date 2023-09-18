package com.example.projectfilrouge.controller;

import com.example.projectfilrouge.dto.TicketDto;
import com.example.projectfilrouge.entity.Ticket;
import com.example.projectfilrouge.service.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Rest controller se base sur du rest (qui est une sous "catégorie" de controller qui lui est vaste et rest pose un cadre qui a JSON comme utilisation parmis tant d'autre)
@RestController
//Constructeurs avec comme paramètre ticketService dans les arguments (faut importer lombook)
@AllArgsConstructor
//Permet de donner le chemin URL aux types de requêtes qui suivent
@RequestMapping("/tickets")
public class TicketController {

    private TicketService ticketService;


//Post mapping permet que ta fonction soit en attente de requête de type post
    //RequestBody c'est le contenu de la requete mais il est utilisé que dans les post et les put
    //Ici le requestBody va aller lire le body de ma requete http et il va créer un objet de type ticketDto
// dans lequel il va mettre à jour les variables présentent pour y enregistrer les informations présentent
// dans le body, et du coup dans JSON de postman dans le post tu vient mettre {eventName : "Gims" et il le
// mettre à l'intérieur de la variable eventName}
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
