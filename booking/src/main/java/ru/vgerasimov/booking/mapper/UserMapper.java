package ru.vgerasimov.booking.mapper;

import org.mapstruct.Mapper;
import ru.vgerasimov.booking.dto.BookingDto;
import ru.vgerasimov.booking.dto.UserDto;
import ru.vgerasimov.booking.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserDto userDto);
    UserDto toDto(User user);
}
