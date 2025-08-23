package ru.Tim.ShiftCRM.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.Tim.ShiftCRM.entity.Seller;

import java.awt.print.Pageable;

public interface SellerRepository extends JpaRepository<Seller, Long> {

    Page<Seller> findAll(Pageable pageable);


}