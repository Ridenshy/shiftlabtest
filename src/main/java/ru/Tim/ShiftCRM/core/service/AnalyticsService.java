package ru.Tim.ShiftCRM.core.service;

import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import ru.Tim.ShiftCRM.api.dto.analytics.request.BadSellerRequest;
import ru.Tim.ShiftCRM.api.dto.analytics.response.SellerBestPeriodResponse;
import ru.Tim.ShiftCRM.api.dto.analytics.response.TopSellerResponse;
import ru.Tim.ShiftCRM.api.dto.seller.responce.SellerDto;


public interface AnalyticsService {

    @Transactional(readOnly = true)
    TopSellerResponse getBestSeller(String datePeriod);

    @Transactional(readOnly = true)
    Page<SellerDto> getBadSellers(int page, int size, BadSellerRequest badSellerRequest);

    @Transactional(readOnly = true)
    SellerBestPeriodResponse getSellerBestPeriod(Long sellerId);

}
