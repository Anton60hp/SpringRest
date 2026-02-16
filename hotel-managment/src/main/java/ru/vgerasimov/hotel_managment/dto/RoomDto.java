package ru.vgerasimov.hotel_managment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {
    private Long id;
    private Long hotelId;
    private String number;
    private Boolean available;
    private Integer timesBooked;
}
