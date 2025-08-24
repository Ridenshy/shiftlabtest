package ru.Tim.ShiftCRM.dto.analytics.response;

import lombok.Builder;
import ru.Tim.ShiftCRM.dto.seller.responce.SellerDto;

import java.math.BigDecimal;

@Builder
public class TopSellerResponse {

    SellerDto topSeller;

    BigDecimal sellerAmount;

}
