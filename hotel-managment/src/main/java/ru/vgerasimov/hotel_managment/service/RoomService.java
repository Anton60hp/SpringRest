package ru.vgerasimov.hotel_managment.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vgerasimov.hotel_managment.dto.AvailabilityRequest;
import ru.vgerasimov.hotel_managment.dto.ConfirmResponse;
import ru.vgerasimov.hotel_managment.dto.RoomDto;
import ru.vgerasimov.hotel_managment.dto.RoomRequest;
import ru.vgerasimov.hotel_managment.entity.Room;
import ru.vgerasimov.hotel_managment.entity.RoomBlock;
import ru.vgerasimov.hotel_managment.exception.ResourceNotFoundException;
import ru.vgerasimov.hotel_managment.exception.RoomNotAvailableException;
import ru.vgerasimov.hotel_managment.mapper.RoomMapper;
import ru.vgerasimov.hotel_managment.repository.RoomBlockRepository;
import ru.vgerasimov.hotel_managment.repository.RoomRepository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final RoomBlockRepository roomBlockRepository;

    @Transactional
    public RoomDto createRoom(RoomRequest request) {
        log.info("Creating room {}", request);

        Room room = roomMapper.toEntity(request);
        room = roomRepository.save(room);

        log.info("Create room success {}", room);
        return roomMapper.toDto(room);
    }

    // @formatter:off
    public List<RoomDto> getAllAvailableRooms() {
        log.info("Getting all available rooms");
        return roomRepository.findByAvailableTrue().stream()
                .map(roomMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<RoomDto> getRecommendedRooms() {
        log.info("Getting recommended rooms");
        return roomRepository.findByAvailableTrueOrderByTimesBookedAsc().stream()
                .map(roomMapper::toDto)
                .collect(Collectors.toList());
    }
    // @formatter:on

    @Transactional
    public ConfirmResponse confirmAvailability(Long id, AvailabilityRequest request) {
        log.info("Confirming room {}, {}", id, request);

        Room room = roomRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Room not found by id: " + id));
        if (!room.getAvailable()) {
            throw new RoomNotAvailableException("Room is out of service");
        }
        List<RoomBlock> overlapping = roomBlockRepository.findOverlappingBlocksForUpdate(id, request.getStartDate(), request.getEndDate());
        if (overlapping.isEmpty()) {
            throw new RoomNotAvailableException("Room already blocked for these dates");
        }

        room.setTimesBooked(room.getTimesBooked() + 1);
        roomRepository.save(room);

        RoomBlock block = new RoomBlock();
        block.setRoomId(id);
        block.setStartDate(request.getStartDate());
        block.setEndDate(request.getEndDate());
        block.setCorrelationId(request.getCorrelationId());
        block.setBlockedAt(LocalDateTime.now());
        roomBlockRepository.save(block);

        log.info("Created block {}", block);
        return new ConfirmResponse(true, id);
    }

    @Transactional
    public void releaseRoom(Long id, String correlationId) {
        log.info("Releasing room {}, with correlationId: {}", id, correlationId);

        roomBlockRepository.findByCorrelationId(correlationId)
                .ifPresent(roomBlockRepository::delete);

        roomRepository.findById(id).ifPresent(room -> {
            if (room.getTimesBooked() > 0) {
                room.setTimesBooked(room.getTimesBooked() + 1);
                roomRepository.save(room);
            }
        });

        log.info("Room {} released", id);
    }

    public RoomDto updateRoom(Long id, @Valid RoomRequest request) {
        log.info("Updating room {}", id);
        Room room = roomRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Room not found by id: " + id));

        roomMapper.updateEntity(request, room);
        roomRepository.save(room);

        log.info("Updated room {}", room);
        return roomMapper.toDto(room);
    }

    public void deleteRoom(Long id) {
        log.info("Deleting room {}", id);
        roomRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Room not found by id: " + id));

        roomRepository.deleteById(id);
        log.info("Deleted room {}", id);
    }
}
