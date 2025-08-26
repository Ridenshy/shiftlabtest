package ru.Tim.ShiftCRM.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.Tim.ShiftCRM.api.controller.AnalyticController;
import ru.Tim.ShiftCRM.core.service.AnalyticsService;
import ru.Tim.ShiftCRM.core.service.SellerService;
import ru.Tim.ShiftCRM.core.service.TransactionService;
import ru.Tim.ShiftCRM.core.service.impl.AnalyticsServiceImpl;
import ru.Tim.ShiftCRM.core.service.impl.SellerServiceImpl;
import ru.Tim.ShiftCRM.core.service.impl.TransactionServiceImpl;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class ControllerConfiguration {

    @Bean
    SellerService sellerService() {
        return mock(SellerServiceImpl.class);
    }

    @Bean
    TransactionService transactionService() {
        return mock(TransactionServiceImpl.class);
    }

    @Bean
    AnalyticsService analyticsService() {
        return mock(AnalyticsServiceImpl.class);
    }

}
