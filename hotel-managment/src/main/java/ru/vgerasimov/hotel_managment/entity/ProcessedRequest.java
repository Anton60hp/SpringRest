package ru.vgerasimov.hotel_managment.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "processed_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessedRequest {
    @Id
    private String correlationId;
    private String endpoint;
    private String responseBody;
    private LocalDateTime processedAt;
}
