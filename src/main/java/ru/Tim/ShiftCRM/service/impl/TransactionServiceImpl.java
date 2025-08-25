package ru.Tim.ShiftCRM.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.Tim.ShiftCRM.dto.transaction.request.NewTransactionDto;
import ru.Tim.ShiftCRM.dto.transaction.response.TransactionDto;
import ru.Tim.ShiftCRM.dto.transaction.mapper.TransactionMapper;
import ru.Tim.ShiftCRM.entity.Seller;
import ru.Tim.ShiftCRM.entity.Transaction;
import ru.Tim.ShiftCRM.repository.SellerRepository;
import ru.Tim.ShiftCRM.repository.TransactionRepository;
import ru.Tim.ShiftCRM.service.TransactionService;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final SellerRepository sellerRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public Page<TransactionDto> getAllTransactions(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.ASC, "transactionDate");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Transaction> transactions = transactionRepository.findAll(pageable);
        return transactions.map(transactionMapper::transactionToTransactionDto);
    }

    @Override
    public TransactionDto getTransactionInfo(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Не было найдено транзакции с id %d", id)));
        return transactionMapper.transactionToTransactionDto(transaction);
    }

    @Override
    public Long createTransaction(NewTransactionDto newTransactionDto) {
        Long sellerId = newTransactionDto.getSellerId();
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Не было найдено продовца с id %d", sellerId)));
        Transaction transaction = transactionMapper
                .newTransactionDtoToTransaction(newTransactionDto, seller);
        transaction.setTransactionDate(LocalDateTime.now());
        return transactionRepository.save(transaction).getId();
    }

    @Override
    public Page<TransactionDto> getTransactionsBySeller(Long sellerId, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.ASC, "transactionDate");
        Pageable pageable = PageRequest.of(page, size, sort);
        sellerRepository.findById(sellerId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Не было найдено продовца с id %d", sellerId)));
        Page<Transaction> transactions = transactionRepository.findBySellerId(sellerId, pageable);
        return transactions.map(transactionMapper::transactionToTransactionDto);
    }
}
