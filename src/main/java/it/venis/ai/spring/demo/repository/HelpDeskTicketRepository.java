package it.venis.ai.spring.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.venis.ai.spring.demo.entity.HelpDeskTicket;

public interface HelpDeskTicketRepository extends JpaRepository<HelpDeskTicket, Long> {

    List<HelpDeskTicket> findByUsername(String username);
    
}