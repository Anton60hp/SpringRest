package ru.vgerasimov.hotel_managment.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilityRequest {

    private String correlationId;
    private LocalDate startDate;
    private LocalDate endDate;

}
