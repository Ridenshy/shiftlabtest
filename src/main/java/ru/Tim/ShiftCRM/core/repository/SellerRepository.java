package ru.Tim.ShiftCRM.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.Tim.ShiftCRM.core.entity.Seller;


public interface SellerRepository extends JpaRepository<Seller, Long> {

    Page<Seller> findAll(Pageable pageable);

    boolean existsByContactInfo(String contactInfo);

}