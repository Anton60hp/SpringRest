package ru.vgerasimov.hotel_managment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.vgerasimov.hotel_managment.dto.HotelDto;
import ru.vgerasimov.hotel_managment.dto.HotelRequest;
import ru.vgerasimov.hotel_managment.entity.Hotel;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface  HotelMapper {
    HotelDto toDto(Hotel hotel);
    Hotel toEntity(HotelRequest request);
    void updateEntity(HotelRequest request, @MappingTarget Hotel hotel);
}
