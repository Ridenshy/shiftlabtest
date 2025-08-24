package ru.Tim.ShiftCRM.dto.analytics.response;


import lombok.Builder;

import java.time.LocalDate;

@Builder
public class SellerBestPeriodResponse {

    private LocalDate startOfPeriod;

    private LocalDate endOfPeriod;

    private Double density;

}
