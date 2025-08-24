package ru.Tim.ShiftCRM.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.Tim.ShiftCRM.entity.Seller;
import ru.Tim.ShiftCRM.entity.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findAll(Pageable pageable);

    Page<Transaction> findBySellerId(Long sellerId, Pageable pageable);

    @Query("SELECT t.seller, SUM(t.amount) as totalAmount " +
            "FROM Transaction t " +
            "WHERE t.transactionDate BETWEEN :start AND :end " +
            "GROUP BY t.seller " +
            "ORDER BY totalAmount DESC LIMIT 1")
    Optional<Object[]> findTopSellerByPeriod(LocalDateTime start, LocalDateTime end);

    @Query("SELECT t.seller FROM Transaction t " +
            "WHERE t.transactionDate BETWEEN :start AND :end " +
            "GROUP BY t.seller " +
            "HAVING SUM(t.amount) < :minAmount")
    Page<Seller> findBadSellers(LocalDate start, LocalDate end, BigDecimal minAmount, Pageable pageable);

    @Query("SELECT DATE(t.transactionDate), COUNT(t) " +
            "FROM Transaction t " +
            "WHERE t.seller.id = :sellerId " +
            "AND DATE(t.transactionDate) BETWEEN :start AND :end " +
            "GROUP BY DATE(t.transactionDate)")
    List<Object[]> findDailyTransactionCounts(LocalDateTime start, LocalDateTime end, Long sellerId);

}