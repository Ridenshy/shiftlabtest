package ru.Tim.ShiftCRM.core.service;

import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import ru.Tim.ShiftCRM.api.model.seller.request.NewSellerRequest;
import ru.Tim.ShiftCRM.api.model.seller.responce.SellerResponse;
import ru.Tim.ShiftCRM.api.model.seller.request.UpdatedSellerRequest;

public interface SellerService {

    @Transactional(readOnly = true)
    Page<SellerResponse> getAllSellers(int page, int size);

    @Transactional(readOnly = true)
    SellerResponse getSellerInfo(Long id);

    @Transactional
    SellerResponse saveNewSeller(NewSellerRequest newSeller);

    @Transactional
    SellerResponse updateSeller(UpdatedSellerRequest updatedSeller, Long id);

    @Transactional
    void deleteSeller(Long sellerId);

}
