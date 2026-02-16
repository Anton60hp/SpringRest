package ru.vgerasimov.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.vgerasimov.booking.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private Long id;
    private User user;
    private Long roomId;
    private Long hotelId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private String correlationId;
    private LocalDateTime createdAt;
}
