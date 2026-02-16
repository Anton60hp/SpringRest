package ru.vgerasimov.hotel_managment.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "room_blocks", indexes = {
        @Index(name = "idx_room_blocks_room_id", columnList = "room_id"),
        @Index(name = "idx_room_blocks_dates", columnList = "start_date,end_date"),
        @Index(name = "idx_room_blocks_correlation_id", columnList = "correlation_id")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "correlation_id", nullable = false, unique = true)
    private String correlationId;

    @CreationTimestamp
    @Column(name = "blocked_at", nullable = false, updatable = false)
    private LocalDateTime blockedAt;

}
