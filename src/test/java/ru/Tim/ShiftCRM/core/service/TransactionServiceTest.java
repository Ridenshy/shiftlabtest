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
import ru.Tim.ShiftCRM.api.dto.transaction.request.NewTransactionDto;
import ru.Tim.ShiftCRM.api.dto.transaction.response.TransactionDto;
import ru.Tim.ShiftCRM.config.TestcontainersConfiguration;
import ru.Tim.ShiftCRM.core.entity.Seller;
import ru.Tim.ShiftCRM.core.entity.Transaction;
import ru.Tim.ShiftCRM.core.enums.PaymentType;
import ru.Tim.ShiftCRM.core.repository.SellerRepository;
import ru.Tim.ShiftCRM.core.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@Testcontainers
@Transactional
public class TransactionServiceTest {

    @Autowired TransactionService transactionService;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp(){
        entityManager.createNativeQuery("truncate table sellers restart identity cascade").executeUpdate();
        entityManager.createNativeQuery("truncate table transactions restart identity cascade").executeUpdate();
        List<Seller> sellers = List.of(
                Seller.builder()
                        .name("Евгений")
                        .contactInfo("evg@gmail.com")
                        .registrationDate(LocalDateTime.now().minusDays(15))
                        .build(),
                Seller.builder()
                        .name("Александр")
                        .contactInfo("+79619998877")
                        .registrationDate(LocalDateTime.now().minusDays(10))
                        .build(),
                Seller.builder()
                        .name("Михаил")
                        .contactInfo("mi@mail.ru")
                        .registrationDate(LocalDateTime.now().minusDays(2))
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
                        .amount(BigDecimal.valueOf(1500))
                        .paymentType(PaymentType.CARD)
                        .seller(sellers.get(0))
                        .transactionDate(LocalDateTime.now().minusDays(3))
                        .build(),
                Transaction.builder()
                        .amount(BigDecimal.valueOf(5000))
                        .paymentType(PaymentType.TRANSFER)
                        .seller(sellers.get(0))
                        .transactionDate(LocalDateTime.now().minusDays(5))
                        .build(),
                Transaction.builder()
                        .amount(BigDecimal.valueOf(300))
                        .paymentType(PaymentType.CASH)
                        .seller(sellers.get(1))
                        .transactionDate(LocalDateTime.now().minusDays(1))
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
                        .transactionDate(LocalDateTime.now().minusDays(1))
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
    void getAllTransactions_returnedPagedTransactions(){
        Page<TransactionDto> page = transactionService.getAllTransactions(0, 10);
        assertNotNull(page);
        assertEquals(10, page.getTotalElements());
        assertEquals(10, page.getContent().size());
        List<TransactionDto> transactions = page.getContent();
        assertEquals(PaymentType.TRANSFER.name(), transactions.get(0).getPaymentType());
        assertEquals(2L, transactions.get(2).getSellerId());
    }

    @Test
    void getAllTransactions_withSecondPage_returnsCorrectResults(){
        Page<TransactionDto> page = transactionService.getAllTransactions(1, 5);
        assertNotNull(page);
        assertEquals(10, page.getTotalElements());
        assertEquals(5, page.getContent().size());
        assertEquals(2, page.getTotalPages());
    }

    @Test
    void getAllTransactions_returnedIncorrectPage(){
        Page<TransactionDto> page = transactionService.getAllTransactions(10, 10);
        assertNotNull(page);
        assertTrue(page.getContent().isEmpty());
        assertEquals(10, page.getTotalElements());
    }

    @Test
    void getTransactionInfo_returnedCorrectDto(){
        TransactionDto transactionDto = transactionService.getTransactionInfo(1L);
        assertNotNull(transactionDto);
        assertEquals(1L, transactionDto.getId());
        assertEquals(PaymentType.CASH.name(), transactionDto.getPaymentType());
        assertEquals(1L, transactionDto.getSellerId());
        assertEquals(BigDecimal.valueOf(500), transactionDto.getAmount());
        assertNotNull(transactionDto.getTransactionDate());
    }

    @Test
    void getTransactionInfo_withNotExistId_throwException(){
        Long id = 999L;
        assertThrows(
                EntityNotFoundException.class,
                () -> transactionService.getTransactionInfo(id)
        );
        assertFalse(transactionRepository.existsById(id));
    }

    @Test
    void createTransaction_withCorrectDto(){
        NewTransactionDto dto = new NewTransactionDto(
                1L,
                BigDecimal.valueOf(500),
                "CASH"
        );
        Long id = transactionService.createTransaction(dto);
        assertNotNull(id);
        assertTrue(transactionRepository.existsById(id));

        Optional<Transaction> createdTransaction = transactionRepository.findById(id);
        assertTrue(createdTransaction.isPresent());
        assertEquals(BigDecimal.valueOf(500), createdTransaction.get().getAmount());
        assertEquals(PaymentType.CASH, createdTransaction.get().getPaymentType());
        assertEquals(1L, createdTransaction.get().getSeller().getId());
        assertNotNull(createdTransaction.get().getTransactionDate());
    }

    @Test
    void createTransaction_withNonExistentSeller_throwException(){
        NewTransactionDto dto = new NewTransactionDto(
                999L,
                BigDecimal.valueOf(500),
                "CASH"
        );

        assertThrows(
                EntityNotFoundException.class,
                () -> transactionService.createTransaction(dto)
        );
    }

    @Test
    void createTransaction_withDifferentPaymentTypes_createsSuccessfully(){
        List<String> paymentTypes = List.of("CASH", "CARD", "TRANSFER");

        for (String paymentType : paymentTypes) {
            NewTransactionDto dto = new NewTransactionDto(
                    1L,
                    BigDecimal.valueOf(100),
                    paymentType
            );

            Long id = transactionService.createTransaction(dto);
            assertNotNull(id);
            assertTrue(transactionRepository.existsById(id));

            Optional<Transaction> transaction = transactionRepository.findById(id);
            assertTrue(transaction.isPresent());
            assertEquals(PaymentType.valueOf(paymentType), transaction.get().getPaymentType());
        }
    }

    @Test
    void getTransactionsBySeller_withValidSellerId_returnsPagedTransactions() {
        Long sellerId = 1L;

        Page<TransactionDto> page = transactionService.getTransactionsBySeller(sellerId, 0, 5);

        assertNotNull(page);
        assertTrue(page.getTotalElements() > 0);
        assertEquals(3, page.getTotalElements());
        assertEquals(1, page.getTotalPages());

        List<TransactionDto> transactions = page.getContent();
        assertEquals(3, transactions.size());

        for (TransactionDto transaction : transactions) {
            assertEquals(sellerId, transaction.getSellerId());
        }

        for (int i = 0; i < transactions.size() - 1; i++) {
            assertTrue(transactions.get(i).getTransactionDate()
                    .isBefore(transactions.get(i + 1).getTransactionDate()) ||
                    transactions.get(i).getTransactionDate()
                            .isEqual(transactions.get(i + 1).getTransactionDate()));
        }
    }

    @Test
    void getTransactionsBySeller_withNonExistentSellerId_throwsException() {
        Long nonExistentSellerId = 999L;

        assertThrows(
                EntityNotFoundException.class,
                () -> transactionService.getTransactionsBySeller(nonExistentSellerId, 0, 10)
        );
    }

    @Test
    void getTransactionsBySeller_withEmptyTransactions_returnsEmptyPage() {
        Seller newSeller = Seller.builder()
                .name("Новый продавец")
                .contactInfo("new@mail.ru")
                .registrationDate(LocalDateTime.now())
                .build();
        sellerRepository.save(newSeller);

        Page<TransactionDto> page = transactionService.getTransactionsBySeller(newSeller.getId(), 0, 10);

        assertNotNull(page);
        assertEquals(0, page.getTotalElements());
        assertTrue(page.getContent().isEmpty());
    }

    @Test
    void getTransactionsBySeller_withNegativePage_usesFirstPage() {
        Long sellerId = 1L;

        Page<TransactionDto> negativePage = transactionService.getTransactionsBySeller(sellerId, -1, 5);
        Page<TransactionDto> firstPage = transactionService.getTransactionsBySeller(sellerId, 0, 5);

        assertNotNull(negativePage);
        assertNotNull(firstPage);
        assertEquals(firstPage.getContent().size(), negativePage.getContent().size());
    }

    @Test
    void getTransactionsBySeller_withZeroSize_throwsException() {
        Long sellerId = 1L;
        assertThrows(
                IllegalArgumentException.class,
                () -> transactionService.getTransactionsBySeller(sellerId, 0, 0)
        );
    }
}
