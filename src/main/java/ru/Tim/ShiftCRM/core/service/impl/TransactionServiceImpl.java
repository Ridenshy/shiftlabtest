package ru.Tim.ShiftCRM.core.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.Tim.ShiftCRM.api.model.transaction.request.NewTransactionRequest;
import ru.Tim.ShiftCRM.api.model.transaction.response.TransactionResponse;
import ru.Tim.ShiftCRM.api.mapper.TransactionMapper;
import ru.Tim.ShiftCRM.core.entity.Seller;
import ru.Tim.ShiftCRM.core.entity.Transaction;
import ru.Tim.ShiftCRM.core.repository.SellerRepository;
import ru.Tim.ShiftCRM.core.repository.TransactionRepository;
import ru.Tim.ShiftCRM.core.service.TransactionService;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final SellerRepository sellerRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public Page<TransactionResponse> getAllTransactions(int page, int size) {
        if(size < 1){
            throw new IllegalArgumentException("Размер страницы должен быть больше 0");
        }
        int pageNum = page < 0 ? 0 : page;
        Sort sort = Sort.by(Sort.Direction.ASC, "transactionDate");
        Pageable pageable = PageRequest.of(pageNum, size, sort);
        Page<Transaction> transactions = transactionRepository.findAll(pageable);
        return transactions.map(transactionMapper::transactionToTransactionDto);
    }

    @Override
    public TransactionResponse getTransactionInfo(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Не было найдено транзакции с id %d", id)));
        return transactionMapper.transactionToTransactionDto(transaction);
    }

    @Override
    public Long createTransaction(NewTransactionRequest newTransactionRequest) {
        Long sellerId = newTransactionRequest.getSellerId();
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Не было найдено продовца с id %d", sellerId)));
        Transaction transaction = transactionMapper
                .newTransactionDtoToTransaction(newTransactionRequest, seller);
        transaction.setTransactionDate(LocalDateTime.now());
        return transactionRepository.save(transaction).getId();
    }

    @Override
    public Page<TransactionResponse> getTransactionsBySeller(Long sellerId, int page, int size) {
        if(size < 1){
            throw new IllegalArgumentException("Размер страницы должен быть больше 0");
        }
        int pageNum = page < 0 ? 0 : page;
        Sort sort = Sort.by(Sort.Direction.ASC, "transactionDate");
        Pageable pageable = PageRequest.of(pageNum, size, sort);
        sellerRepository.findById(sellerId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Не было найдено продовца с id %d", sellerId)));
        Page<Transaction> transactions = transactionRepository.findBySellerId(sellerId, pageable);
        return transactions.map(transactionMapper::transactionToTransactionDto);
    }
}
