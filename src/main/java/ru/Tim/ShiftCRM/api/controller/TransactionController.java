package ru.Tim.ShiftCRM.api.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.Tim.ShiftCRM.api.swagger.OpenApi.TransactionApi;
import ru.Tim.ShiftCRM.api.dto.transaction.request.NewTransactionDto;
import ru.Tim.ShiftCRM.api.dto.transaction.response.TransactionDto;
import ru.Tim.ShiftCRM.core.service.TransactionService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/apiV1/transactions/")
@Validated
public class TransactionController implements TransactionApi {

    private final TransactionService transactionService;

    @Override
    @GetMapping()
    public ResponseEntity<Page<TransactionDto>> getAll(
            @RequestParam(defaultValue = "0")
            @PositiveOrZero(message = "Параметр page должен быть больше или равен 0")
            int page,
            @RequestParam(defaultValue = "50")
            @Positive(message = "Параметр size должен быть больше 0")
            int size
            ) {
        Page<TransactionDto> transactions = transactionService.getAllTransactions(page, size);
        return ResponseEntity.ok(transactions);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<TransactionDto> getTransactionInfo(@PathVariable Long id) {
        TransactionDto transaction = transactionService.getTransactionInfo(id);
        return ResponseEntity.ok(transaction);
    }

    @Override
    @PostMapping()
    public ResponseEntity<String> createTransaction(@RequestBody @Validated NewTransactionDto newTransactionDto) {
        Long id = transactionService.createTransaction(newTransactionDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(String.format("Транзакция создана с id %d", id));
    }

    @Override
    @GetMapping("/sellerTransactions/{id}")
    public ResponseEntity<Page<TransactionDto>> getSellerTransactions(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0")
            @PositiveOrZero(message = "Параметр page должен быть больше или равен 0")
            int page,
            @RequestParam(defaultValue = "50")
            @Positive(message = "Параметр size должен быть больше 0")
            int size) {
     Page<TransactionDto> transactions = transactionService.getTransactionsBySeller(id, page, size);
     return ResponseEntity.ok(transactions);
    }

}
