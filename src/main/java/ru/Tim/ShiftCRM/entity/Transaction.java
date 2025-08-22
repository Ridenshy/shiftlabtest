package ru.Tim.ShiftCRM.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.Tim.ShiftCRM.enums.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne()
    Seller seller;

    @Column(nullable = false)
    BigDecimal amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    PaymentType paymentType;

    @Column(nullable = false)
    LocalDateTime transactionDate;

}
