package org.ironhack.project.controllers;

import org.ironhack.project.models.classes.Booking;
import org.ironhack.project.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping
    public List<Booking> findAll() {
        return bookingService.findAll();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Booking> findById(@PathVariable Integer userId) {
        Optional<Booking> booking = bookingService.findById(userId);
        return booking.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
    }

    @PostMapping("/new")
    public ResponseEntity<Booking> create(@RequestBody Booking booking) {
        Booking newBooking = bookingService.create(booking);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBooking);
    }
}
