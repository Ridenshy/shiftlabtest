package ru.Tim.ShiftCRM.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.Tim.ShiftCRM.core.entity.Seller;
import ru.Tim.ShiftCRM.core.entity.Transaction;

import java.math.BigDecimal;
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

    @Query("SELECT s FROM Seller s " +
            "WHERE (SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
            "WHERE t.seller.id = s.id AND t.transactionDate BETWEEN :start AND :end) < :minAmount")
    Page<Seller> findBadSellers(LocalDateTime start, LocalDateTime end, BigDecimal minAmount, Pageable pageable);

    @Query("SELECT CAST(t.transactionDate AS date), COUNT(t) " +
            "FROM Transaction t " +
            "WHERE t.seller.id = :sellerId " +
            "AND t.transactionDate BETWEEN :start AND :end " +
            "GROUP BY CAST(t.transactionDate AS date)" +
            "ORDER BY CAST(t.transactionDate AS date) ASC")
    List<Object[]> findDailyTransactionCounts(LocalDateTime start, LocalDateTime end, Long sellerId);

}