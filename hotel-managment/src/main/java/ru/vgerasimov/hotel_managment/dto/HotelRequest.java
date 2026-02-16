package ru.vgerasimov.hotel_managment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelRequest {
    @NotBlank(message = "Hotel name is required")
    @Size(max = 150, message = "Hotel name must not exceed 150 characters")
    private String name;

    @NotBlank(message = "Hotel address is required")
    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String address;
}