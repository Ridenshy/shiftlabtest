package ru.Tim.ShiftCRM.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.Tim.ShiftCRM.dto.analytics.request.BadSellerRequest;
import ru.Tim.ShiftCRM.dto.analytics.response.SellerBestPeriodResponse;
import ru.Tim.ShiftCRM.dto.analytics.response.TopSellerResponse;
import ru.Tim.ShiftCRM.dto.seller.mapper.SellerMapper;
import ru.Tim.ShiftCRM.dto.seller.responce.SellerDto;
import ru.Tim.ShiftCRM.entity.Seller;
import ru.Tim.ShiftCRM.repository.SellerRepository;
import ru.Tim.ShiftCRM.repository.TransactionRepository;
import ru.Tim.ShiftCRM.service.AnalyticsService;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private final SellerRepository sellerRepository;
    private final TransactionRepository transactionRepository;

    private final SellerMapper sellerMapper;

    @Override
    public TopSellerResponse getBestSeller(String datePeriod) {
        LocalDateTime[] period = getPeriod(datePeriod);
        LocalDateTime start = period[0];
        LocalDateTime end = period[1];
        Object[] result = transactionRepository.findTopSellerByPeriod(start, end)
                .orElseThrow(
                        () -> new EntityNotFoundException("Не было найдено продавца за заданный период")
                );
        Object[] data = (Object[]) result[0];
        Seller topSeller = (Seller) data[0];
        BigDecimal total = (BigDecimal) data[1];

        SellerDto topSellerDto = sellerMapper.sellerToSellerDto(topSeller);
        return TopSellerResponse.builder()
                .topSeller(topSellerDto)
                .sellerAmount(total)
                .build();
    }

    @Override
    public Page<SellerDto> getBadSellers(int page, int size, BadSellerRequest badSellerRequest) {
        Sort sort = Sort.by(Sort.Direction.ASC, "registrationDate");
        Pageable pageable = PageRequest.of(page, size, sort);

        LocalDateTime minDate = LocalDateTime.of(badSellerRequest.getMinDate(), LocalTime.MIN);
        LocalDateTime maxDate = LocalDateTime.of(badSellerRequest.getMaxDate(), LocalTime.MAX);

        Page<Seller> sellers = transactionRepository.findBadSellers(
                minDate,
                maxDate,
                badSellerRequest.getMinAmount(),
                pageable
                );

        return sellers.map(sellerMapper::sellerToSellerDto);
    }

    @Override
    public SellerBestPeriodResponse getSellerBestPeriod(Long sellerId) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Не было найдено продовца с id %d", sellerId)));

        LocalDate registrationDate = seller.getRegistrationDate().toLocalDate();
        LocalDate today = LocalDate.now();
        if(registrationDate.isAfter(today)) {
            throw new IllegalArgumentException("Дата регистрации продовца не может быть больше текущей");
        }

        return findBestDensityPeriod(registrationDate, today, sellerId);
    }

    private SellerBestPeriodResponse findBestDensityPeriod(LocalDate startDate, LocalDate endDate, Long sellerId) {
        Map<LocalDate, Integer> dailyTransactions = getDailyTransactions(sellerId, startDate, endDate);

        double maxValue = -1;
        int maxLength = 0;
        LocalDate currentStart = null;
        LocalDate bestStartDate = startDate;
        LocalDate bestEndDate = startDate;

        LocalDate currentDate = startDate;
        int currentLength = 0;

        while (!currentDate.isAfter(endDate)) {
            int transactions = dailyTransactions.getOrDefault(currentDate, 0);

            if (transactions > maxValue) {
                maxValue = transactions;
                maxLength = 0;
                currentLength = 0;
                currentStart = null;
            }

            if (transactions == maxValue) {
                if (currentLength == 0) {
                    currentStart = currentDate;
                }
                currentLength++;

                if (currentLength > maxLength) {
                    maxLength = currentLength;
                    bestStartDate = currentStart;
                    bestEndDate = currentDate;
                }
            } else {
                currentLength = 0;
            }
            currentDate = currentDate.plusDays(1);
        }

        return SellerBestPeriodResponse.builder()
                .startOfPeriod(bestStartDate)
                .endOfPeriod(bestEndDate)
                .density(maxValue)
                .build();
    }


    private Map<LocalDate, Integer> getDailyTransactions(Long sellerId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        List<Object[]> results = transactionRepository.findDailyTransactionCounts(startDateTime, endDateTime, sellerId);

        Map<LocalDate, Integer> dailyMap = new LinkedHashMap<>();
        for (Object[] result : results) {
            LocalDate date = ((Date) result[0]).toLocalDate();
            Long count = (Long) result[1];
            dailyMap.put(date, count.intValue());
        }
        if (dailyMap.isEmpty()) {
            throw new EntityNotFoundException("Не было найдено транзакций у данного продовца");
        }
        return dailyMap;
    }

    private LocalDateTime[] getPeriod(String datePeriod) {
        LocalDateTime now = LocalDateTime.now();

        return switch (datePeriod.toUpperCase()) {
            case "DAY" -> new LocalDateTime[]{
                    now.toLocalDate().atStartOfDay(),
                    now.toLocalDate().atTime(LocalTime.MAX)
            };
            case "WEEK" -> new LocalDateTime[]{
                    now.toLocalDate().minusDays(now.getDayOfWeek().getValue() - 1).atStartOfDay(),
                    now.toLocalDate().atTime(LocalTime.MAX)
            };
            case "MONTH" -> new LocalDateTime[]{
                    now.toLocalDate().withDayOfMonth(1).atStartOfDay(),
                    now.toLocalDate().atTime(LocalTime.MAX)
            };
            case "QUARTER" -> {
                int currentQuarter = (now.getMonthValue() - 1) / 3 + 1;
                LocalDate quarterStart = LocalDate.of(now.getYear(), (currentQuarter - 1) * 3 + 1, 1);
                yield new LocalDateTime[]{
                        quarterStart.atStartOfDay(),
                        now.toLocalDate().atTime(LocalTime.MAX)
                };
            }
            case "YEAR" -> new LocalDateTime[]{
                    LocalDate.of(now.getYear(), 1, 1).atStartOfDay(),
                    now.toLocalDate().atTime(LocalTime.MAX)
            };
            default -> throw new IllegalArgumentException("Не верный формат периода: " + datePeriod);
        };
    }
}
