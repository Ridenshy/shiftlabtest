package ru.Tim.ShiftCRM.dto.transaction.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import ru.Tim.ShiftCRM.annotation.validation.IsEnum;
import ru.Tim.ShiftCRM.enums.PaymentType;

import java.math.BigDecimal;

@Value
public class NewTransactionDto {

    @NotNull
    Long sellerId;

    @NotNull
    BigDecimal amount;

    @IsEnum(enumClass = PaymentType.class)
    @NotBlank
    String paymentType;

}
