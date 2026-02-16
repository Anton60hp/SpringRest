package ru.vgerasimov.booking.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.vgerasimov.booking.dto.ConfirmAvailabilityRequest;
import ru.vgerasimov.booking.dto.ConfirmAvailabilityResponse;

@FeignClient(
        name = "hotel-management",
        path = "/api/rooms"
)
public interface HotelManagementClient {

    @PostMapping("/{roomId}/confirm-availability")
    ConfirmAvailabilityResponse confirmAvailability(
            @PathVariable("roomId") Long roomId,
            @RequestBody ConfirmAvailabilityRequest request
    );

    @PostMapping("/{roomId}/release")
    void releaseRoom(
            @PathVariable("roomId") Long roomId,
            @RequestParam("requestId") String correlationId
    );
}
