package ru.Tim.ShiftCRM.core.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.Tim.ShiftCRM.core.enums.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seller_id", nullable = false)
    Seller seller;

    @Column(nullable = false)
    BigDecimal amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    PaymentType paymentType;

    @Column(nullable = false)
    LocalDateTime transactionDate;

}
