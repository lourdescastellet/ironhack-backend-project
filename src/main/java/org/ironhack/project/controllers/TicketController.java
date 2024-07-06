package org.ironhack.project.controllers;

import jakarta.persistence.Tuple;
import org.ironhack.project.models.classes.Ticket;
import org.ironhack.project.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;

//    TODO
//
//    @GetMapping
//    public List<Ticket> findAll() {
//        return ticketService.findAll();
//    }

//    @GetMapping("/{userId}")
//    public ResponseEntity<Ticket> findById(@PathVariable Integer userId) {
//        Optional<Ticket> ticket =ticketService.findById(userId);
//        return ticket.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
//    }

//    @PostMapping("/new")
//    public ResponseEntity<Ticket> create(@RequestBody Ticket ticket) {
//        Ticket newTicket =ticketService.create(ticket);
//        return ResponseEntity.status(HttpStatus.CREATED).body(newTicket);
//    }
}
