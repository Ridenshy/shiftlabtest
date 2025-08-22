package ru.Tim.ShiftCRM.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.Tim.ShiftCRM.repository.TransactionRepository;
import ru.Tim.ShiftCRM.service.TransactionService;

@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

}
