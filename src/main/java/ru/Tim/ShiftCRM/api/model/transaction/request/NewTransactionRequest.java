package ru.Tim.ShiftCRM.api.model.transaction.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import ru.Tim.ShiftCRM.api.annotation.IsEnum;
import ru.Tim.ShiftCRM.core.enums.PaymentType;

import java.math.BigDecimal;

@Value
public class NewTransactionRequest {

    @NotNull(message = "Поле не должно быть пустым")
    Long sellerId;

    @NotNull(message = "Поле не должно быть пустым")
    BigDecimal amount;

    @IsEnum(enumClass = PaymentType.class, message = "Поле подлжно содержать значение: CASH, CARD, TRANSFER")
    @NotBlank(message = "Поле не должно быть пустым")
    @NotNull
    String paymentType;

}
