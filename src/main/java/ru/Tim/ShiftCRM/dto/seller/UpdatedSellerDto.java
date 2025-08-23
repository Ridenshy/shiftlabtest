package ru.Tim.ShiftCRM.dto.seller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class UpdatedSellerDto {

    @Size(min = 2, max = 50, message = "name could be in range 2 to 50 characters")
    String name;

    String contactInfo;

    LocalDateTime registrationDate;

}
