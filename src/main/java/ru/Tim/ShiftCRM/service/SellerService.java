package ru.Tim.ShiftCRM.service;

import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import ru.Tim.ShiftCRM.dto.seller.NewSellerDto;
import ru.Tim.ShiftCRM.dto.seller.SellerDto;
import ru.Tim.ShiftCRM.dto.seller.UpdatedSellerDto;

import java.awt.print.Pageable;

public interface SellerService {

    @Transactional(readOnly = true)
    Page<SellerDto> getAllSellers(Pageable pageable);

    SellerDto getSellerInfo(Long id);

    void saveNewSeller(NewSellerDto newSeller);

    void updateSeller(UpdatedSellerDto updatedSeller, Long id);

    void deleteSeller(Long sellerId);

}
