package ru.Tim.ShiftCRM.api.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.Tim.ShiftCRM.api.annotation.IsEnum;
import ru.Tim.ShiftCRM.api.swagger.OpenApi.AnalyticApi;
import ru.Tim.ShiftCRM.api.model.analytics.SellerBestPeriodResponse;
import ru.Tim.ShiftCRM.api.model.analytics.TopSellerResponse;
import ru.Tim.ShiftCRM.api.model.seller.responce.SellerResponse;
import ru.Tim.ShiftCRM.core.enums.DatePeriod;
import ru.Tim.ShiftCRM.core.service.AnalyticsService;

import java.math.BigDecimal;
import java.time.LocalDate;


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
            String datePeriodType
    ) {
        return ResponseEntity.ok(analyticsService.getBestSeller(datePeriodType));
    }

    @Override
    @GetMapping("/getBadSellers")
    public ResponseEntity<Page<SellerResponse>> getBadSellers(
            @RequestParam(defaultValue = "0")
            @PositiveOrZero(message = "Параметр page должен быть больше или равен 0")
            int page,
            @RequestParam(defaultValue = "50")
            @Positive(message = "Параметр size должен быть больше 0")
            int size,
            @RequestParam
            @NotNull(message = "Параметр minAmount не должен быть Null")
            BigDecimal minAmount,
            @RequestParam
            @NotNull
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate minDate,
            @NotNull
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate maxDate
    ) {
       return ResponseEntity.ok(analyticsService.getBadSellers(page, size, minAmount, minDate, maxDate));
    }

    @Override
    @GetMapping("/getSellerBestPeriod")
    public ResponseEntity<SellerBestPeriodResponse> getSellerBestPeriod(
            @RequestParam
            @NotNull(message = "Параметр sellerId не должен быть Null")
            Long sellerId
    ) {
        return ResponseEntity.ok(analyticsService.getSellerBestPeriod(sellerId));
    }

}
