package ru.Tim.ShiftCRM.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.Tim.ShiftCRM.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}