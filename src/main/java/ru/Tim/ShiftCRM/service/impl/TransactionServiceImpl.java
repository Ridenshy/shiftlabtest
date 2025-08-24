package ru.Tim.ShiftCRM.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.Tim.ShiftCRM.dto.transaction.NewTransactionDto;
import ru.Tim.ShiftCRM.dto.transaction.TransactionDto;
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
        return null;
    }

    @Override
    public TransactionDto getTransactionInfo(Long id) {
        Transaction transaction = transactionRepository
                .findTransactionBySellerId(id);
        return transactionMapper.transactionToTransactionDto(transaction);
    }

    @Override
    public void createTransaction(NewTransactionDto newTransactionDto) {
        Long sellerId = newTransactionDto.getSellerId();
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("No such seller with id: %d", sellerId)));
        Transaction transaction = transactionMapper
                .newTransactionDtoToTransaction(newTransactionDto, seller);
        transaction.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(transaction);
    }

    @Override
    public TransactionDto getTransactionBySeller(Long sellerId) {
        return null;
    }
}
