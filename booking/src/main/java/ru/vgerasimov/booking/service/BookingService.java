package ru.vgerasimov.booking.service;


import feign.FeignException;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vgerasimov.booking.dto.BookingDto;
import ru.vgerasimov.booking.dto.BookingRequest;
import ru.vgerasimov.booking.dto.ConfirmAvailabilityRequest;
import ru.vgerasimov.booking.dto.ConfirmAvailabilityResponse;
import ru.vgerasimov.booking.entity.Booking;
import ru.vgerasimov.booking.entity.User;
import ru.vgerasimov.booking.exception.ResourceNotFoundException;
import ru.vgerasimov.booking.exception.RoomNotAvailableException;
import ru.vgerasimov.booking.exception.UserNotFoundException;
import ru.vgerasimov.booking.feignClient.HotelManagementClient;
import ru.vgerasimov.booking.mapper.BookingMapper;
import ru.vgerasimov.booking.repository.BookingRepository;
import ru.vgerasimov.booking.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService {

    BookingRepository bookingRepository;
    BookingMapper bookingMapper;
    UserRepository userRepository;
    HotelManagementClient hotelManagementClient;

    @Transactional
    @Retry(name = "hotelService", fallbackMethod = "createBookingFallback")
    public BookingDto createBooking(String username, @Valid BookingRequest request) {
        log.info("Create booking request: {} - by: {}", request, username);

        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));

        validateBookingRequest(request);


        String correlationId = request.getCorrelationId() != null
                ? request.getCorrelationId() : UUID.randomUUID().toString();


        if (bookingRepository.existsByCorrelationId(correlationId)) {
            log.info("Booking already exists for correlationId: {}", correlationId);
            return bookingMapper.toDto(bookingRepository.findByCorrelationId(correlationId).orElse(null));
        }

        // @formatter:off
        Booking booking = Booking.builder()
                .user(user)
                .roomId(request.getRoomId())
                .hotelId(request.getHotelId())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(Booking.BookingStatus.PENDING)
                .correlationId(correlationId)
                .build();
        // @formatter:on
        booking = bookingRepository.save(booking);
        log.info("Created booking: {}", booking);

        try {

            ConfirmAvailabilityRequest confirmRequest = ConfirmAvailabilityRequest.builder()
                    .startDate(request.getStartDate())
                    .endDate(request.getEndDate())
                    .bookingId(booking.getId())
                    .correlationId(correlationId)
                    .build();
            ConfirmAvailabilityResponse response = hotelManagementClient.confirmAvailability(request.getRoomId(), confirmRequest);
            if (response.getAvailable()) {
                booking.setStatus(Booking.BookingStatus.CONFIRMED);
                bookingRepository.save(booking);
                log.info("Booking has been confirmed: {}", booking);
            } else {
                booking.setStatus(Booking.BookingStatus.CANCELLED);
                bookingRepository.save(booking);
                throw new RoomNotAvailableException("Room is not available: " + response.getMessage());
            }

        } catch (FeignException e) {
            booking.setStatus(Booking.BookingStatus.CANCELLED);
            bookingRepository.save(booking);

            throw new RoomNotAvailableException("Failed to confirm booking: " + e.getMessage());
        }
        return bookingMapper.toDto(booking);
    }


    private void validateBookingRequest(@Valid BookingRequest request) {
        if (request.getStartDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Start date cannot be in the past");
        }

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new IllegalArgumentException("End date must be after start date");
        }

        if (request.getEndDate().equals(request.getStartDate())) {
            throw new IllegalArgumentException("Booking must be at least one night");
        }
    }

    public List<BookingDto> getUserBookings(String username) {
        log.info("Get user bookings for username: {}", username);
        return bookingRepository.findByUserId_Username(username).stream()
                .map(bookingMapper::toDto)
                .collect(Collectors.toList());
    }

    public BookingDto getBookingById(Long id, String username) {
        log.info("Get booking by id: {} - by username: {}", id, username);
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
        return bookingMapper.toDto(booking);
    }

    @Transactional
    public void cancelBooking(Long id, String username) {
        log.info("Cancel booking by id: {} - by username: {}", id, username);

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!booking.getUser().equals(user)) {
            throw new IllegalArgumentException("You are not authorized to cancel this booking");
        }

        if (booking.getStatus() == Booking.BookingStatus.CONFIRMED) {
            try {
                hotelManagementClient.releaseRoom(booking.getRoomId(), booking.getCorrelationId());
                log.info("Room released successfully: {}", booking.getRoomId());
            } catch (FeignException e) {
                log.error("Failed to release room in hotel service: {}", e.getMessage());
            }


        }

        if (booking.getStatus() == Booking.BookingStatus.CANCELLED) {
            throw new IllegalArgumentException("Booking is already cancelled");
        }

        booking.setStatus(Booking.BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        log.info("Booking cancelled successfully: {}", id);

    }
}
