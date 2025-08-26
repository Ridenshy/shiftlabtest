package ru.Tim.ShiftCRM.api.dto.transaction.response;


import lombok.Value;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
public class TransactionDto {

    Long id;

    Long sellerId;

    BigDecimal amount;

    String paymentType;

    LocalDateTime transactionDate;

}
