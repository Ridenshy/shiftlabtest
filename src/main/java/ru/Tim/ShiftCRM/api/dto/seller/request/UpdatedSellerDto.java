package ru.Tim.ShiftCRM.api.dto.seller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;
import ru.Tim.ShiftCRM.api.annotation.ContactInfo;

@Value
public class UpdatedSellerDto {

    @NotBlank(message = "Поле не должно быть пустым")
    @Size(min = 2, max = 50, message = "Имя должно быть от 2 до 50 символов")
    String name;

    @ContactInfo(message = "Поле должно быть валидным email или номером телефона")
    @NotBlank(message = "Поле не должно быть пустым")
    String contactInfo;

}
