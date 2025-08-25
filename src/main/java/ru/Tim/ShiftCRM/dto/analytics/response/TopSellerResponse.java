package ru.Tim.ShiftCRM.dto.analytics.response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.Tim.ShiftCRM.dto.seller.responce.SellerDto;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class TopSellerResponse {

    SellerDto topSeller;

    @NotNull
    BigDecimal sellerAmount;

}
