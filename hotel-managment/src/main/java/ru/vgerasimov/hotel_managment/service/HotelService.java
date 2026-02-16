package ru.vgerasimov.hotel_managment.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vgerasimov.hotel_managment.dto.HotelDto;
import ru.vgerasimov.hotel_managment.dto.HotelRequest;
import ru.vgerasimov.hotel_managment.entity.Hotel;
import ru.vgerasimov.hotel_managment.exception.ResourceNotFoundException;
import ru.vgerasimov.hotel_managment.mapper.HotelMapper;
import ru.vgerasimov.hotel_managment.repository.HotelRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HotelService {
    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    @Transactional
    public HotelDto createHotel(HotelRequest request) {
        log.info("createHotel: request={}", request);

        Hotel hotel = hotelMapper.toEntity(request);
        hotel = hotelRepository.save(hotel);

        log.info("createHotel success: hotel={}", hotel);
        return hotelMapper.toDto(hotel);
    }

    public List<HotelDto> getAllHotels() {
        log.info("getAllHotels");
        return hotelRepository.findAll().stream()
                .map(hotelMapper::toDto)
                .collect(Collectors.toList());
    }

    public HotelDto getHotelById(Long id) {
        log.info("getHotelById: id={}", id);
        return hotelMapper.toDto(hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id)));
    }

}
