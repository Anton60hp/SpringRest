package ru.vgerasimov.hotel_managment.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.vgerasimov.hotel_managment.dto.RoomDto;
import ru.vgerasimov.hotel_managment.dto.RoomRequest;
import ru.vgerasimov.hotel_managment.entity.Room;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RoomMapper {

    RoomDto toDto(Room room);

    @Mapping(target = "timesBooked", constant = "0")
    Room toEntity(RoomRequest roomRequest);

    void updateEntity( RoomRequest roomDto, @MappingTarget Room room);

}
