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

    @NotBlank(message = "Поле не должно быть пустым")
    Long id;

    @NotNull(message = "Поле не должно быть пустым")
    Long sellerId;

    @NotNull(message = "Поле не должно быть пустым")
    BigDecimal amount;

    @IsEnum(enumClass = PaymentType.class, message = "Поле подлжно содержать значение: CASH, CARD, TRANSFER")
    @NotBlank(message = "Поле не должно быть пустым")
    String paymentType;

    @NotNull
    LocalDateTime transactionDate;

}
