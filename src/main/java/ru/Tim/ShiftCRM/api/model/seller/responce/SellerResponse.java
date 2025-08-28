package ru.Tim.ShiftCRM.api.model.seller.responce;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class SellerResponse {

    Long id;

    String name;

    String contactInfo;

    LocalDateTime registrationDate;

}
