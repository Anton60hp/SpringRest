package ru.vgerasimov.booking.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmAvailabilityRequest {
    private LocalDate startDate;
    private LocalDate endDate;
    private Long bookingId;
    private String correlationId;
}
