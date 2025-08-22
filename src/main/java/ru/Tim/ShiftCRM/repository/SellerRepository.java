package ru.Tim.ShiftCRM.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.Tim.ShiftCRM.entity.Seller;

public interface SellerRepository extends JpaRepository<Seller, Long> {
}