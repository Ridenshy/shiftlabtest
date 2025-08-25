package ru.Tim.ShiftCRM.dto.analytics.response;


import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class SellerBestPeriodResponse {

    @NotNull
    private LocalDate startOfPeriod;

    @NotNull
    private LocalDate endOfPeriod;

    @NotNull
    private Double density;

}
