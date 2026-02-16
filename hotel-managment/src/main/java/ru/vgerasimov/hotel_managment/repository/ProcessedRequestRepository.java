package ru.vgerasimov.hotel_managment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vgerasimov.hotel_managment.entity.ProcessedRequest;

import java.util.Optional;

@Repository
public interface ProcessedRequestRepository extends JpaRepository<ProcessedRequest, Long> {

    Optional<ProcessedRequest> findByCorrelationId(String correlationId);
}
