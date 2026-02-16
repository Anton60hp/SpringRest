package ru.vgerasimov.booking.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.vgerasimov.booking.dto.BookingDto;
import ru.vgerasimov.booking.dto.BookingRequest;
import ru.vgerasimov.booking.service.BookingService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BookingDto> createBooking(@Valid @RequestBody BookingRequest request,
                                                    Authentication authentication) {
        String username = authentication.getName();
        log.info("POST /bookings - request: {} - by: {}", request, username);
        BookingDto booking = bookingService.createBooking(username, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(booking);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<BookingDto>> getUserBookings(Authentication authentication) {
        String username = authentication.getName();
        log.info("GET /bookings - by: {}", username);
        List<BookingDto> bookings = bookingService.getUserBookings(username);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BookingDto> getBooking(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        log.info("GET /bookings/{} - by: {}", id, username);
        BookingDto booking = bookingService.getBookingById(id, username);
        return ResponseEntity.ok(booking);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        log.info("DELETE /bookings/{} - by: {}", id, username);
        bookingService.cancelBooking(id, username);
        return ResponseEntity.ok().build();
    }
}
