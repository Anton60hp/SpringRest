package ru.vgerasimov.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vgerasimov.booking.dto.BookingDto;
import ru.vgerasimov.booking.entity.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    boolean existsByCorrelationId(String correlationId);

    Optional<Booking> findByCorrelationId(String correlationId);

    List<Booking> findByUserId_Username(String username);

}
