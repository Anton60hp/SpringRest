package ru.vgerasimov.hotel_managment.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.vgerasimov.hotel_managment.entity.Room;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmResponse {
    private boolean success;
    private Long roomId;
}
