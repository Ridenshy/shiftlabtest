package ru.Tim.ShiftCRM.dto.seller.responce;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;
import ru.Tim.ShiftCRM.annotation.validation.ContactInfo;

import java.time.LocalDateTime;

@Value
public class SellerDto {

    @NotNull(message = "Поле не должно быть пустым")
    Long id;

    @NotBlank(message = "Поле не должно быть пустым")
    @Size(min = 2, max = 50, message = "Имя должно быть от 2 до 50 символов")
    String name;

    @NotBlank(message = "Поле не должно быть пустым")
    @ContactInfo(message = "Поле должно быть валидным email или номером телефона")
    String contactInfo;

    @NotNull(message = "Поле не должно быть пустым")
    LocalDateTime registrationDate;

}
