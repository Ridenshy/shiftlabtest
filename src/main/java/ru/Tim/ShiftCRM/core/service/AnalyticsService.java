package ru.Tim.ShiftCRM.core.service;

import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import ru.Tim.ShiftCRM.api.model.analytics.SellerBestPeriodResponse;
import ru.Tim.ShiftCRM.api.model.analytics.TopSellerResponse;
import ru.Tim.ShiftCRM.api.model.seller.responce.SellerResponse;

import java.math.BigDecimal;
import java.time.LocalDate;


public interface AnalyticsService {

    @Transactional(readOnly = true)
    TopSellerResponse getBestSeller(String datePeriod);

    @Transactional(readOnly = true)
    Page<SellerResponse> getBadSellers(int page, int size, BigDecimal minAmount, LocalDate minDate, LocalDate maxDate);

    @Transactional(readOnly = true)
    SellerBestPeriodResponse getSellerBestPeriod(Long sellerId);

}
