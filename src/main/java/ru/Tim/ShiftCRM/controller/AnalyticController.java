package ru.Tim.ShiftCRM.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.Tim.ShiftCRM.annotation.validation.IsEnum;
import ru.Tim.ShiftCRM.dto.analytics.request.BadSellerRequest;
import ru.Tim.ShiftCRM.dto.analytics.response.SellerBestPeriodResponse;
import ru.Tim.ShiftCRM.dto.analytics.response.TopSellerResponse;
import ru.Tim.ShiftCRM.dto.seller.responce.SellerDto;
import ru.Tim.ShiftCRM.entity.Seller;
import ru.Tim.ShiftCRM.entity.Transaction;
import ru.Tim.ShiftCRM.enums.DatePeriod;
import ru.Tim.ShiftCRM.enums.PaymentType;
import ru.Tim.ShiftCRM.repository.SellerRepository;
import ru.Tim.ShiftCRM.repository.TransactionRepository;
import ru.Tim.ShiftCRM.service.AnalyticsService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@RequiredArgsConstructor
@RequestMapping("/apiV1/analytic/")
public class AnalyticController {

    private final AnalyticsService analyticsService;

    private final TransactionRepository transactionRepository;
    private final SellerRepository sellerRepository;

    @GetMapping("/getTopSeller/")
    public ResponseEntity<TopSellerResponse> getTopSeller(
            @IsEnum(enumClass = DatePeriod.class) String datePeriodType) {
        return ResponseEntity.ok(analyticsService.getBestSeller(datePeriodType));
    }

    @GetMapping("/getBadSellers")
    public ResponseEntity<Page<SellerDto>> getBadSellers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestBody @Validated BadSellerRequest badSellerRequest) {
       return ResponseEntity.ok(analyticsService.getBadSellers(page, size, badSellerRequest));
    }


    @GetMapping("/getSellerBestPeriod")
    public ResponseEntity<SellerBestPeriodResponse> getSellerBestPeriod(
            @RequestParam Long sellerId) {
        return ResponseEntity.ok(analyticsService.getSellerBestPeriod(sellerId));
    }

    @PostMapping("/test")
    public String test() {
        Seller seller = sellerRepository.findById(1L).orElseThrow();

        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0 ,0, 0);
        LocalDateTime endDate = LocalDateTime.now();

        Random random = new Random();

        List<Transaction> transactions = new ArrayList<>();
        LocalDateTime currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            int transactionsCount = random.nextInt(21);

            for (int i = 0; i < transactionsCount; i++) {
                Transaction transaction = new Transaction();
                transaction.setSeller(seller);
                transaction.setAmount(generateRandomAmount(random));
                transaction.setPaymentType(generateRandomPaymentType(random));
                transaction.setTransactionDate(generateRandomDateTime(currentDate, random));

                transactions.add(transaction);
            }

            currentDate = currentDate.plusDays(1);
        }

        // Сохраняем все транзакции
        transactionRepository.saveAll(transactions);

        return String.format("Успешно создано %d транзакций с %s по %s",
                transactions.size(), startDate, endDate);
    }

    private BigDecimal generateRandomAmount(Random random) {
        double amount = 10.00 + (random.nextDouble() * 990.00);
        return BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP);
    }

    private PaymentType generateRandomPaymentType(Random random) {
        PaymentType[] types = PaymentType.values();
        return types[random.nextInt(types.length)];
    }

    private LocalDateTime generateRandomDateTime(LocalDateTime date, Random random) {
        int hour = random.nextInt(24);
        int minute = random.nextInt(60);
        int second = random.nextInt(60);
        int nanoOfSecond = random.nextInt(1_000_000_000); // наносекунды от 0 до 999,999,999

        return LocalDateTime.of(
                date.toLocalDate(),
                LocalTime.of(hour, minute, second, nanoOfSecond)
        );
    }

}
