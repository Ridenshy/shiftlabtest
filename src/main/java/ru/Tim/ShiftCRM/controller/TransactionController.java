package ru.Tim.ShiftCRM.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.Tim.ShiftCRM.configuration.OpenApi.TransactionApi;
import ru.Tim.ShiftCRM.dto.transaction.request.NewTransactionDto;
import ru.Tim.ShiftCRM.dto.transaction.response.TransactionDto;
import ru.Tim.ShiftCRM.service.TransactionService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/apiV1/transaction")
@Validated
public class TransactionController implements TransactionApi {

    private final TransactionService transactionService;

    @Override
    @GetMapping("/getAll")
    public ResponseEntity<Page<TransactionDto>> getAll(@RequestParam(defaultValue = "0")
                                                       @PositiveOrZero int page,
                                                       @RequestParam(defaultValue = "50")
                                                       @Positive int size) {
        Page<TransactionDto> transactions = transactionService.getAllTransactions(page, size);
        return ResponseEntity.ok(transactions);
    }

    @Override
    @GetMapping("getInfo/{id}")
    public ResponseEntity<TransactionDto> getTransactionInfo(@PathVariable Long id) {
        TransactionDto transaction = transactionService.getTransactionInfo(id);
        return ResponseEntity.ok(transaction);
    }

    @Override
    @PostMapping("/create")
    public ResponseEntity<String> createTransaction(@RequestBody @Validated NewTransactionDto newTransactionDto) {
        Long id = transactionService.createTransaction(newTransactionDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(String.format("Транзакция создана с id %d", id));
    }

    @Override
    @GetMapping("/getSellerTransactions/{id}")
    public ResponseEntity<Page<TransactionDto>> getSellerTransactions(@PathVariable Long id,
                                                                      @RequestParam(defaultValue = "0")
                                                                      @PositiveOrZero int page,
                                                                      @RequestParam(defaultValue = "50")
                                                                      @Positive int size) {
     Page<TransactionDto> transactions = transactionService.getTransactionsBySeller(id, page, size);
     return ResponseEntity.ok(transactions);
    }

}
