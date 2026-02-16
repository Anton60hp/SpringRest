package ru.vgerasimov.hotel_managment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vgerasimov.hotel_managment.entity.Room;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByAvailableTrue();

    List<Room> findByHotel_Id(Long id);

    List<Room> findByAvailableTrueOrderByTimesBookedAsc();
}
