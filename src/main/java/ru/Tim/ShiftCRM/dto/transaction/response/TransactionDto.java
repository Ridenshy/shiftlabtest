package ru.Tim.ShiftCRM.dto.transaction.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import ru.Tim.ShiftCRM.annotation.validation.IsEnum;
import ru.Tim.ShiftCRM.enums.PaymentType;

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

    @IsEnum(enumClass = PaymentType.class)
    @NotBlank
    String paymentType;

    @NotNull
    LocalDateTime transactionDate;

}
