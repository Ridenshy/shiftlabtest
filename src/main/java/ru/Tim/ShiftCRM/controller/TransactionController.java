package ru.Tim.ShiftCRM.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.Tim.ShiftCRM.dto.transaction.request.NewTransactionDto;
import ru.Tim.ShiftCRM.dto.transaction.response.TransactionDto;
import ru.Tim.ShiftCRM.service.TransactionService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/apiV1/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/getAll")
    public ResponseEntity<Page<TransactionDto>> getAll(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "50") int size) {
        Page<TransactionDto> transactions = transactionService.getAllTransactions(page, size);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("getInfo/{id}")
    public ResponseEntity<TransactionDto> getTransactionInfo(@PathVariable Long id) {
        TransactionDto transaction = transactionService.getTransactionInfo(id);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createTransaction(@RequestBody @Validated NewTransactionDto newTransactionDto) {
        transactionService.createTransaction(newTransactionDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/getSellerTransactions/{id}")
    public ResponseEntity<Page<TransactionDto>> getSellerTransactions(@PathVariable Long id,
                                                                      @RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "50") int size) {
     Page<TransactionDto> transactions = transactionService.getTransactionsBySeller(id, page, size);
     return ResponseEntity.ok(transactions);
    }



}
