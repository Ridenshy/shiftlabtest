package ru.Tim.ShiftCRM.api.model.analytics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.Tim.ShiftCRM.api.model.seller.responce.SellerResponse;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class TopSellerResponse {

    SellerResponse topSeller;

    BigDecimal sellerAmount;

}
