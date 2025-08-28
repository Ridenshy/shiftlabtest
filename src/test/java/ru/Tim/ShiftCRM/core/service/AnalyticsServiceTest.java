package ru.Tim.ShiftCRM.core.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.Tim.ShiftCRM.api.model.analytics.SellerBestPeriodResponse;
import ru.Tim.ShiftCRM.api.model.analytics.TopSellerResponse;
import ru.Tim.ShiftCRM.api.model.seller.responce.SellerResponse;
import ru.Tim.ShiftCRM.config.TestcontainersConfiguration;
import ru.Tim.ShiftCRM.core.entity.Seller;
import ru.Tim.ShiftCRM.core.entity.Transaction;
import ru.Tim.ShiftCRM.core.enums.PaymentType;
import ru.Tim.ShiftCRM.core.repository.SellerRepository;
import ru.Tim.ShiftCRM.core.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Import(TestcontainersConfiguration.class)
@Testcontainers
@Transactional
@SpringBootTest
public class AnalyticsServiceTest {

    @Autowired
    private AnalyticsService analyticsService;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private EntityManager entityManager;

    private List<Seller> sellers;

    @BeforeEach
    void setUp() {
        entityManager.createNativeQuery("truncate table sellers restart identity cascade").executeUpdate();
        entityManager.createNativeQuery("truncate table transactions restart identity cascade").executeUpdate();

        sellers = List.of(
                Seller.builder()
                        .name("Евгений")
                        .contactInfo("evg@gmail.com")
                        .registrationDate(LocalDateTime.now().minusDays(30))
                        .build(),
                Seller.builder()
                        .name("Александр")
                        .contactInfo("+79619998877")
                        .registrationDate(LocalDateTime.now().minusDays(30))
                        .build(),
                Seller.builder()
                        .name("Михаил")
                        .contactInfo("mi@mail.ru")
                        .registrationDate(LocalDateTime.now().minusDays(40))
                        .build()
        );
        sellerRepository.saveAll(sellers);

        List<Transaction> transactions = List.of(
                Transaction.builder()
                        .amount(BigDecimal.valueOf(500))
                        .paymentType(PaymentType.CASH)
                        .seller(sellers.get(0))
                        .transactionDate(LocalDateTime.now().minusDays(1))
                        .build(),
                Transaction.builder()
                        .amount(BigDecimal.valueOf(5000))
                        .paymentType(PaymentType.CARD)
                        .seller(sellers.get(0))
                        .transactionDate(LocalDateTime.now().minusDays(3))
                        .build(),
                Transaction.builder()
                        .amount(BigDecimal.valueOf(5000))
                        .paymentType(PaymentType.TRANSFER)
                        .seller(sellers.get(0))
                        .transactionDate(LocalDateTime.now().minusDays(3))
                        .build(),
                Transaction.builder()
                        .amount(BigDecimal.valueOf(3000))
                        .paymentType(PaymentType.TRANSFER)
                        .seller(sellers.get(0))
                        .transactionDate(LocalDateTime.now().minusDays(2))
                        .build(),
                Transaction.builder()
                        .amount(BigDecimal.valueOf(6000))
                        .paymentType(PaymentType.TRANSFER)
                        .seller(sellers.get(0))
                        .transactionDate(LocalDateTime.now().minusDays(2))
                        .build(),
                Transaction.builder()
                        .amount(BigDecimal.valueOf(300))
                        .paymentType(PaymentType.CASH)
                        .seller(sellers.get(1))
                        .transactionDate(LocalDateTime.now().minusDays(0))
                        .build(),
                Transaction.builder()
                        .amount(BigDecimal.valueOf(900))
                        .paymentType(PaymentType.CARD)
                        .seller(sellers.get(1))
                        .transactionDate(LocalDateTime.now().minusDays(3))
                        .build(),
                Transaction.builder()
                        .amount(BigDecimal.valueOf(3600))
                        .paymentType(PaymentType.TRANSFER)
                        .seller(sellers.get(1))
                        .transactionDate(LocalDateTime.now().minusDays(5))
                        .build(),
                Transaction.builder()
                        .amount(BigDecimal.valueOf(500))
                        .paymentType(PaymentType.CASH)
                        .seller(sellers.get(2))
                        .transactionDate(LocalDateTime.now().minusDays(0))
                        .build(),
                Transaction.builder()
                        .amount(BigDecimal.valueOf(2000))
                        .paymentType(PaymentType.CARD)
                        .seller(sellers.get(2))
                        .transactionDate(LocalDateTime.now().minusDays(3))
                        .build(),
                Transaction.builder()
                        .amount(BigDecimal.valueOf(4000))
                        .paymentType(PaymentType.CARD)
                        .seller(sellers.get(2))
                        .transactionDate(LocalDateTime.now().minusDays(5))
                        .build(),
                Transaction.builder()
                        .amount(BigDecimal.valueOf(7000))
                        .paymentType(PaymentType.TRANSFER)
                        .seller(sellers.get(2))
                        .transactionDate(LocalDateTime.now().minusDays(7))
                        .build()
        );
        transactionRepository.saveAll(transactions);
    }

    @Test
    void getBestSeller_withDayPeriod_returnsTopSeller() {

        assertDoesNotThrow(() -> {
            TopSellerResponse response = analyticsService.getBestSeller("DAY");
            assertNotNull(response);
            assertNotNull(response.getTopSeller());
            assertNotNull(response.getSellerAmount());
        });
    }

    @Test
    void getBestSeller_withWeekPeriod_returnsTopSeller() {
        assertDoesNotThrow(() -> {
            TopSellerResponse response = analyticsService.getBestSeller("WEEK");
            assertNotNull(response);
        });
    }

    @Test
    void getBestSeller_withMonthPeriod_returnsTopSeller() {
        assertDoesNotThrow(() -> {
            TopSellerResponse response = analyticsService.getBestSeller("MONTH");
            assertNotNull(response);
        });
    }

    @Test
    void getBestSeller_withQuarterPeriod_returnsTopSeller() {
        assertDoesNotThrow(() -> {
            TopSellerResponse response = analyticsService.getBestSeller("QUARTER");
            assertNotNull(response);
        });
    }

    @Test
    void getBestSeller_withYearPeriod_returnsTopSeller() {
        assertDoesNotThrow(() -> {
            TopSellerResponse response = analyticsService.getBestSeller("YEAR");
            assertNotNull(response);
        });
    }

    @Test
    void getBestSeller_withInvalidPeriod_throwsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> analyticsService.getBestSeller("INVALID")
        );
    }

    @Test
    void getBestSeller_withNoTransactionsInPeriod_throwsException() {
        entityManager.createNativeQuery("truncate table transactions restart identity cascade").executeUpdate();

        assertThrows(
                EntityNotFoundException.class,
                () -> analyticsService.getBestSeller("DAY")
        );
    }

    @Test
    void getBadSellers_withValidRequest_returnsPagedResults() {


        Page<SellerResponse> page = analyticsService.getBadSellers(0, 10,
                BigDecimal.valueOf(1000), LocalDate.now().minusDays(10), LocalDate.now());

        assertNotNull(page);
        assertTrue(page.getTotalElements() >= 0);
    }

    @Test
    void getBadSellers_withNegativePage_usesFirstPage() {

        Page<SellerResponse> negativePage = analyticsService.getBadSellers(-1, 10,
                BigDecimal.valueOf(1000), LocalDate.now().minusDays(10), LocalDate.now());
        Page<SellerResponse> firstPage = analyticsService.getBadSellers(0, 10,
                BigDecimal.valueOf(1000), LocalDate.now().minusDays(10), LocalDate.now());

        assertNotNull(negativePage);
        assertNotNull(firstPage);
        assertEquals(firstPage.getContent().size(), negativePage.getContent().size());
    }

    @Test
    void getBadSellers_withZeroSize_throwsException() {

        assertThrows(
                IllegalArgumentException.class,
                () -> analyticsService.getBadSellers(0, 0,
                        BigDecimal.valueOf(1000), LocalDate.now().minusDays(10), LocalDate.now())
        );

    }

    @Test
    void getBadSellers_withNoMatchingSellers_returnsEmptyPage() {

        Page<SellerResponse> page = analyticsService.getBadSellers(0, 10,
                BigDecimal.valueOf(1000), LocalDate.now().minusDays(10), LocalDate.now());

        assertNotNull(page);
        assertEquals(0, page.getTotalElements());
        assertTrue(page.getContent().isEmpty());
    }

    @Test
    void getSellerBestPeriod_withValidSellerId_returnsBestPeriod() {
        Long sellerId = 1L;
        Double density = 2.0;

        SellerBestPeriodResponse response = analyticsService.getSellerBestPeriod(sellerId);

        assertNotNull(response);
        assertNotNull(response.getStartOfPeriod());
        assertNotNull(response.getEndOfPeriod());
        assertEquals(LocalDate.now().minusDays(3), response.getStartOfPeriod());
        assertEquals(LocalDate.now().minusDays(2), response.getEndOfPeriod());
        assertEquals(density, response.getDensity());
    }

    @Test
    void getSellerBestPeriod_withNonExistentSellerId_throwsException() {
        Long nonExistentSellerId = 999L;

        assertThrows(
                EntityNotFoundException.class,
                () -> analyticsService.getSellerBestPeriod(nonExistentSellerId)
        );
    }

    @Test
    void getSellerBestPeriod_withFutureRegistrationDate_throwsException() {
        Seller futureSeller = Seller.builder()
                .name("Будущий продавец")
                .contactInfo("future@mail.ru")
                .registrationDate(LocalDateTime.now().plusDays(10))
                .build();
        sellerRepository.save(futureSeller);

        assertThrows(
                IllegalArgumentException.class,
                () -> analyticsService.getSellerBestPeriod(futureSeller.getId())
        );
    }

    @Test
    void getSellerBestPeriod_withNoTransactions_throwsException() {
        Seller sellerWithoutTransactions = Seller.builder()
                .name("Продавец без транзакций")
                .contactInfo("notrans@mail.ru")
                .registrationDate(LocalDateTime.now().minusDays(5))
                .build();
        sellerRepository.save(sellerWithoutTransactions);

        assertThrows(
                EntityNotFoundException.class,
                () -> analyticsService.getSellerBestPeriod(sellerWithoutTransactions.getId())
        );
    }

    @Test
    void getSellerBestPeriod_withSingleTransaction_returnsCorrectPeriod() {
        Seller seller = Seller.builder()
                .name("Одиночный продавец")
                .contactInfo("single@mail.ru")
                .registrationDate(LocalDateTime.now().minusDays(10))
                .build();
        sellerRepository.save(seller);

        Transaction transaction = Transaction.builder()
                .amount(BigDecimal.valueOf(1000))
                .paymentType(PaymentType.CASH)
                .seller(seller)
                .transactionDate(LocalDateTime.now().minusDays(5))
                .build();
        transactionRepository.save(transaction);

        SellerBestPeriodResponse response = analyticsService.getSellerBestPeriod(seller.getId());

        assertNotNull(response);
        assertEquals(1.0, response.getDensity());
        assertEquals(response.getStartOfPeriod(), response.getEndOfPeriod());
    }

}
