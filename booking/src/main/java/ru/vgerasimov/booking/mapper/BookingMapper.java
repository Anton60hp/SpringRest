package ru.vgerasimov.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.vgerasimov.booking.dto.BookingDto;
import ru.vgerasimov.booking.entity.Booking;

@Mapper(componentModel = "spring")
public interface  BookingMapper {

    @Mapping(target = "status", expression = "java(booking.getStatus().name())")
    BookingDto toDto(Booking booking);
}
