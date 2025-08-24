package ru.Tim.ShiftCRM.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.Tim.ShiftCRM.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findAll(Pageable pageable);

    @Query("SELECT t FROM Transaction t JOIN FETCH t.seller WHERE t.seller.id = :sellerId")
    Transaction findTransactionBySellerId(Long sellerId);

}