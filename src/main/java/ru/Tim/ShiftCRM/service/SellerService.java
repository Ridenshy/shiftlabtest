package ru.Tim.ShiftCRM.service;

import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import ru.Tim.ShiftCRM.dto.seller.NewSellerDto;
import ru.Tim.ShiftCRM.dto.seller.SellerDto;
import ru.Tim.ShiftCRM.dto.seller.UpdatedSellerDto;

public interface SellerService {

    @Transactional(readOnly = true)
    Page<SellerDto> getAllSellers(int page, int size);

    @Transactional(readOnly = true)
    SellerDto getSellerInfo(Long id);

    @Transactional
    void saveNewSeller(NewSellerDto newSeller);

    @Transactional
    void updateSeller(UpdatedSellerDto updatedSeller, Long id);

    @Transactional
    void deleteSeller(Long sellerId);

}
