package ru.Tim.ShiftCRM.api.dto.analytics.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.Tim.ShiftCRM.api.dto.seller.responce.SellerDto;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class TopSellerResponse {

    SellerDto topSeller;

    BigDecimal sellerAmount;

}
