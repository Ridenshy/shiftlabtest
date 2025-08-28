package ru.Tim.ShiftCRM.core.service;

import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import ru.Tim.ShiftCRM.api.model.transaction.request.NewTransactionRequest;
import ru.Tim.ShiftCRM.api.model.transaction.response.TransactionResponse;

public interface TransactionService {

    @Transactional(readOnly = true)
    Page<TransactionResponse> getAllTransactions(int page, int size);

    @Transactional(readOnly = true)
    TransactionResponse getTransactionInfo(Long id);

    @Transactional
    Long createTransaction(NewTransactionRequest newTransactionRequest);

    @Transactional(readOnly = true)
    Page<TransactionResponse> getTransactionsBySeller(Long sellerId, int page, int size);

}
