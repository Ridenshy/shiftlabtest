package ru.Tim.ShiftCRM.dto.transaction;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import ru.Tim.ShiftCRM.annotation.validation.IsPaymentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
public class TransactionDto {

    @NotBlank
    Long id;

    @NotNull
    Long sellerId;

    @NotNull
    BigDecimal amount;

    @IsPaymentType
    @NotBlank
    String paymentType;

    @NotNull
    LocalDateTime transactionDate;

}
