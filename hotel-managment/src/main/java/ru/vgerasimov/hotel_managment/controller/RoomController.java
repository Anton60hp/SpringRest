package ru.vgerasimov.hotel_managment.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vgerasimov.hotel_managment.dto.AvailabilityRequest;
import ru.vgerasimov.hotel_managment.dto.ConfirmResponse;
import ru.vgerasimov.hotel_managment.dto.RoomDto;
import ru.vgerasimov.hotel_managment.service.IdempotencyService;
import ru.vgerasimov.hotel_managment.service.RoomService;


import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final IdempotencyService idempotencyService;



    @GetMapping
    public ResponseEntity<List<RoomDto>> getAllAvailableRooms() {
        log.info("GET /api/rooms");
        List<RoomDto> rooms = roomService.getAllAvailableRooms();
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/recommend")
    public ResponseEntity<List<RoomDto>> getRecommendedRooms() {
        log.info("GET /api/rooms/recommend");
        List<RoomDto> rooms = roomService.getRecommendedRooms();
        return ResponseEntity.ok(rooms);
    }

    @PostMapping("/{id}/confirm-availability")
    public ResponseEntity<ConfirmResponse> confirmAvailability(@PathVariable Long id,
                                                       @Valid @RequestBody AvailabilityRequest request){
        log.info("POST /api/rooms/{}/confirm-availability - request: {}", id, request);
        ConfirmResponse response = idempotencyService.process(
                request.getCorrelationId(),
                "/confirm-availability",
                () -> roomService.confirmAvailability(id, request),  ConfirmResponse.class);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/release")
    public ResponseEntity<Void> releaseRoom(
            @PathVariable Long id,
            @RequestParam String correlationId) {

        log.info("POST /api/rooms/{}/release - correlationId: {}", id, correlationId);

        roomService.releaseRoom(id, correlationId);
        return ResponseEntity.ok().build();
    }
}
