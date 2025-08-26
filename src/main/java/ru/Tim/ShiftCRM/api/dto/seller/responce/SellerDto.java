package ru.Tim.ShiftCRM.api.dto.seller.responce;


import lombok.Value;


import java.time.LocalDateTime;

@Value
public class SellerDto {

    Long id;

    String name;

    String contactInfo;

    LocalDateTime registrationDate;

}
