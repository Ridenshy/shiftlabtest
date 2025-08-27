package ru.Tim.ShiftCRM.api.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.Tim.ShiftCRM.api.annotation.IsEnum;
import ru.Tim.ShiftCRM.api.swagger.OpenApi.AnalyticApi;
import ru.Tim.ShiftCRM.api.dto.analytics.request.BadSellerRequest;
import ru.Tim.ShiftCRM.api.dto.analytics.response.SellerBestPeriodResponse;
import ru.Tim.ShiftCRM.api.dto.analytics.response.TopSellerResponse;
import ru.Tim.ShiftCRM.api.dto.seller.responce.SellerDto;
import ru.Tim.ShiftCRM.core.enums.DatePeriod;
import ru.Tim.ShiftCRM.core.service.AnalyticsService;



@RestController
@RequiredArgsConstructor
@RequestMapping("/apiV1/analytics")
@Validated
public class AnalyticController implements AnalyticApi {

    private final AnalyticsService analyticsService;

    @Override
    @GetMapping("/getTopSeller/")
    public ResponseEntity<TopSellerResponse> getTopSeller(
            @RequestParam
            @NotNull(message = "Параметр datePeriodType не должно быть Null")
            @IsEnum(enumClass = DatePeriod.class,
                    message = "Параметр datePeriodType должно быть: DAY, WEEK, MONTH, QUARTER, YEAR")
            String datePeriodType) {

        return ResponseEntity.ok(analyticsService.getBestSeller(datePeriodType));
    }

    @Override
    @GetMapping("/getBadSellers")
    public ResponseEntity<Page<SellerDto>> getBadSellers(
            @RequestParam(defaultValue = "0")
            @PositiveOrZero(message = "Параметр page должен быть больше или равен 0")
            int page,
            @RequestParam(defaultValue = "50")
            @Positive(message = "Параметр size должен быть больше 0")
            int size,
            @RequestBody
            @Validated
            BadSellerRequest badSellerRequest) {

       return ResponseEntity.ok(analyticsService.getBadSellers(page, size, badSellerRequest));
    }

    @Override
    @GetMapping("/getSellerBestPeriod")
    public ResponseEntity<SellerBestPeriodResponse> getSellerBestPeriod(
            @RequestParam
            @NotNull(message = "Параметр sellerId не должен быть Null")
            Long sellerId) {

        return ResponseEntity.ok(analyticsService.getSellerBestPeriod(sellerId));
    }

}
