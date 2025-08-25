package ru.Tim.ShiftCRM.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.Tim.ShiftCRM.annotation.validation.IsEnum;
import ru.Tim.ShiftCRM.configuration.OpenApi.AnalyticApi;
import ru.Tim.ShiftCRM.dto.analytics.request.BadSellerRequest;
import ru.Tim.ShiftCRM.dto.analytics.response.SellerBestPeriodResponse;
import ru.Tim.ShiftCRM.dto.analytics.response.TopSellerResponse;
import ru.Tim.ShiftCRM.dto.seller.responce.SellerDto;
import ru.Tim.ShiftCRM.enums.DatePeriod;
import ru.Tim.ShiftCRM.service.AnalyticsService;



@RestController
@RequiredArgsConstructor
@RequestMapping("/apiV1/analytic/")
public class AnalyticController implements AnalyticApi {

    private final AnalyticsService analyticsService;

    @Override
    @GetMapping("/getTopSeller/")
    public ResponseEntity<TopSellerResponse> getTopSeller(
            @RequestParam @Validated @IsEnum(enumClass = DatePeriod.class) String datePeriodType) {
        return ResponseEntity.ok(analyticsService.getBestSeller(datePeriodType));
    }

    @Override
    @GetMapping("/getBadSellers")
    public ResponseEntity<Page<SellerDto>> getBadSellers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestBody @Validated BadSellerRequest badSellerRequest) {
       return ResponseEntity.ok(analyticsService.getBadSellers(page, size, badSellerRequest));
    }

    @Override
    @GetMapping("/getSellerBestPeriod")
    public ResponseEntity<SellerBestPeriodResponse> getSellerBestPeriod(
            @RequestParam Long sellerId) {
        return ResponseEntity.ok(analyticsService.getSellerBestPeriod(sellerId));
    }

}
