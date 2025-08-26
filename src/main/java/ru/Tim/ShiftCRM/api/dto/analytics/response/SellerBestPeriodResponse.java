package ru.Tim.ShiftCRM.api.dto.analytics.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class SellerBestPeriodResponse {

    private LocalDate startOfPeriod;

    private LocalDate endOfPeriod;

    private Double density;

}
