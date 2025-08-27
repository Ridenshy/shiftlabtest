package ru.Tim.ShiftCRM.api.dto.transaction.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class TransactionDto {

    Long id;

    Long sellerId;

    BigDecimal amount;

    String paymentType;

    LocalDateTime transactionDate;

}
