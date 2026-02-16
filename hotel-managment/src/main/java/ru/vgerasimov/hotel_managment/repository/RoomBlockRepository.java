package ru.vgerasimov.hotel_managment.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.vgerasimov.hotel_managment.entity.RoomBlock;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomBlockRepository extends JpaRepository<RoomBlock, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT rb FROM RoomBlock rb WHERE rb.roomId = :roomId " +
            "AND rb.startDate < :endDate AND rb.endDate > :startDate")
    List<RoomBlock> findOverlappingBlocksForUpdate(Long roomId, LocalDate startDate, LocalDate endDate);

    Optional<RoomBlock> findByCorrelationId(String correlationId);

}
