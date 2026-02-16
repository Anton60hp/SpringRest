package ru.vgerasimov.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmAvailabilityResponse {
    private Boolean available;
    private String message;
    private String correlationId;
}
