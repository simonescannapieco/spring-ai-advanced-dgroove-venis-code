package it.venis.ai.spring.demo.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import it.venis.ai.spring.demo.entity.HelpDeskTicket;
import it.venis.ai.spring.demo.model.TicketRequest;
import it.venis.ai.spring.demo.repository.HelpDeskTicketRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HelpDeskTicketService {

    private final HelpDeskTicketRepository helpDeskTicketRepository;

    public HelpDeskTicket createTicket(TicketRequest ticketInput, String username) {

        HelpDeskTicket ticket = HelpDeskTicket.builder()
                .issue(ticketInput.issue())
                .username(username)
                .status("OPEN")
                .createdAt(LocalDateTime.now())
                .eta(LocalDateTime.now().plusDays(7))
                .build();

        return helpDeskTicketRepository.save(ticket);
        
    }

    public List<HelpDeskTicket> getTicketsByUsername(String username) {

        return helpDeskTicketRepository.findByUsername(username);

    }

}