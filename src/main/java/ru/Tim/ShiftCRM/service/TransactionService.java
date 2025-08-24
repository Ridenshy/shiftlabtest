package ru.Tim.ShiftCRM.service;

import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import ru.Tim.ShiftCRM.dto.transaction.request.NewTransactionDto;
import ru.Tim.ShiftCRM.dto.transaction.response.TransactionDto;

public interface TransactionService {

    @Transactional(readOnly = true)
    Page<TransactionDto> getAllTransactions(int page, int size);

    @Transactional(readOnly = true)
    TransactionDto getTransactionInfo(Long id);

    @Transactional
    void createTransaction(NewTransactionDto newTransactionDto);

    @Transactional(readOnly = true)
    Page<TransactionDto> getTransactionsBySeller(Long sellerId, int page, int size);

}
