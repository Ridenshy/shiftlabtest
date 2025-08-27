package ru.Tim.ShiftCRM.api.dto.seller.responce;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;


import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class SellerDto {

    Long id;

    String name;

    String contactInfo;

    LocalDateTime registrationDate;

}
