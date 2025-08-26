package ru.Tim.ShiftCRM.core.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sellers")
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String contactInfo;

    @Column(nullable = false)
    private LocalDateTime registrationDate;

    @OneToMany(mappedBy = "seller", orphanRemoval = true, cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    List<Transaction> transactions = new ArrayList<>();

}
