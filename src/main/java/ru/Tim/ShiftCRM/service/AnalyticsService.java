package ru.Tim.ShiftCRM.service;

import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import ru.Tim.ShiftCRM.dto.analytics.request.BadSellerRequest;
import ru.Tim.ShiftCRM.dto.analytics.response.SellerBestPeriodResponse;
import ru.Tim.ShiftCRM.dto.analytics.response.TopSellerResponse;
import ru.Tim.ShiftCRM.dto.seller.responce.SellerDto;


public interface AnalyticsService {

    @Transactional(readOnly = true)
    TopSellerResponse getBestSeller(String datePeriod);

    @Transactional(readOnly = true)
    Page<SellerDto> getBadSellers(int page, int size, BadSellerRequest badSellerRequest);

    @Transactional(readOnly = true)
    SellerBestPeriodResponse getSellerBestPeriod(Long sellerId);

}
