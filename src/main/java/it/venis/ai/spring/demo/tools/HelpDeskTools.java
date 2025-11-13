package it.venis.ai.spring.demo.tools;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import it.venis.ai.spring.demo.entity.HelpDeskTicket;
import it.venis.ai.spring.demo.model.TicketRequest;
import it.venis.ai.spring.demo.services.HelpDeskTicketService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HelpDeskTools {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelpDeskTools.class);

    private final HelpDeskTicketService service;

    @Tool(name = "createTicket", description = "Genera un nuovo ticket di assistenza.", returnDirect = true)
    String createTicket(@ToolParam(description = "Il problema segnalato dall'utente.")
            TicketRequest ticketRequest, ToolContext toolContext) {

        String username = (String) toolContext.getContext().get("username");
        LOGGER.info("Creazione ticket di assistenza per utente {} con dettagli {}", username, ticketRequest);
        HelpDeskTicket savedTicket = service.createTicket(ticketRequest,username);
        LOGGER.info("Ticket creato con successo. Ticket ID: {}, Username: {}", savedTicket.getId(), savedTicket.getUsername());
        return "Ticket #" + savedTicket.getId() + " creato con successo per utente " + savedTicket.getUsername();
    
    }

    @Tool(description = "Recupera lo stato dei ticket di assistenza a partire da uno specifico username.")
    List<HelpDeskTicket> getTicketStatus(ToolContext toolContext) {

        String username = (String) toolContext.getContext().get("username");
        LOGGER.info("Recupero ticket per utente: {}", username);
        List<HelpDeskTicket> tickets =  service.getTicketsByUsername(username);
        LOGGER.info("Trovati {} ticket di assistenza per utente {}", tickets.size(), username);
        return tickets;
    
    }

}