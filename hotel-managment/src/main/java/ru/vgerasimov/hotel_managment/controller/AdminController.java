package ru.vgerasimov.hotel_managment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.vgerasimov.hotel_managment.dto.HotelDto;
import ru.vgerasimov.hotel_managment.dto.HotelRequest;
import ru.vgerasimov.hotel_managment.dto.RoomDto;
import ru.vgerasimov.hotel_managment.dto.RoomRequest;
import ru.vgerasimov.hotel_managment.service.HotelService;
import ru.vgerasimov.hotel_managment.service.RoomService;

@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final HotelService hotelService;
    private final RoomService roomService;

    @PostMapping("/hotels")
    public ResponseEntity<HotelDto> createHotel(@Valid @RequestBody HotelRequest request) {
        log.info("POST /api/hotels - request: {}", request);
        HotelDto hotel = hotelService.createHotel(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(hotel);
    }

    @PostMapping("/rooms")
    public ResponseEntity<RoomDto> createRoom(@Valid @RequestBody RoomRequest request) {
        log.info("POST /api/rooms - request: {}", request);
        RoomDto room = roomService.createRoom(request);
        log.info("Created room: {}", room);
        return ResponseEntity.status(HttpStatus.CREATED).body(room);
    }

    @PutMapping("/rooms/{id}")
    public ResponseEntity<RoomDto> updateRoom(@PathVariable Long id, @Valid @RequestBody RoomRequest request) {
        log.info("PUT /api/rooms/{} - request: {}", id, request);
        RoomDto room = roomService.updateRoom(id, request);
        return ResponseEntity.ok(room);
    }
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        log.info("DELETE /api/rooms/{}", id);
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }


}
