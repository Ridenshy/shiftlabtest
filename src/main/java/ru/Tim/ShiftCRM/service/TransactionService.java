package ru.Tim.ShiftCRM.service;

import org.springframework.data.domain.Page;
import ru.Tim.ShiftCRM.dto.transaction.NewTransactionDto;
import ru.Tim.ShiftCRM.dto.transaction.TransactionDto;

public interface TransactionService {

    Page<TransactionDto> getAllTransactions(int page, int size);

    TransactionDto getTransactionInfo(Long id);

    void createTransaction(NewTransactionDto newTransactionDto);

    TransactionDto getTransactionBySeller(Long sellerId);

}
