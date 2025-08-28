package ru.Tim.ShiftCRM.api.model.transaction.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class TransactionResponse {

    Long id;

    Long sellerId;

    BigDecimal amount;

    String paymentType;

    LocalDateTime transactionDate;

}
