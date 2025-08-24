package ru.Tim.ShiftCRM.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.Tim.ShiftCRM.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findAll(Pageable pageable);

    Page<Transaction> findBySellerId(Long sellerId, Pageable pageable);

}