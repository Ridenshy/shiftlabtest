package ru.Tim.ShiftCRM.dto.transaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import ru.Tim.ShiftCRM.annotation.validation.IsPaymentType;

import java.math.BigDecimal;

@Value
public class UpdatedTransactionDto {

    @NotNull
    Long sellerId;

    @NotNull
    BigDecimal amount;

    @IsPaymentType
    @NotBlank
    String paymentType;

}
