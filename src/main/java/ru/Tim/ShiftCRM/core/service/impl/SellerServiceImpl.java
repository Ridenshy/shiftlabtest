package ru.Tim.ShiftCRM.core.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.Tim.ShiftCRM.api.model.seller.request.NewSellerRequest;
import ru.Tim.ShiftCRM.api.model.seller.responce.SellerResponse;
import ru.Tim.ShiftCRM.api.model.seller.request.UpdatedSellerRequest;
import ru.Tim.ShiftCRM.api.mapper.SellerMapper;
import ru.Tim.ShiftCRM.core.entity.Seller;
import ru.Tim.ShiftCRM.api.exception.ContactInfoAlreadyExistsException;
import ru.Tim.ShiftCRM.core.repository.SellerRepository;
import ru.Tim.ShiftCRM.core.service.SellerService;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;
    private final SellerMapper sellerMapper;

    @Override
    public Page<SellerResponse> getAllSellers(int page, int size) {
        if(size < 1){
            throw new IllegalArgumentException("Размер страницы должен быть больше 0");
        }
        int pageNum = page < 0 ? 0 : page;
        Sort sort = Sort.by(Sort.Direction.ASC, "registrationDate");
        Pageable pageable = PageRequest.of(pageNum, size, sort);
        Page<Seller> sellers = sellerRepository.findAll(pageable);
        return sellers.map(sellerMapper::sellerToSellerDto);
    }

    @Override
    public SellerResponse getSellerInfo(Long id) {
        Seller seller = sellerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Не было найдено продавца с id %d", id)));
        return sellerMapper.sellerToSellerDto(seller);
    }

    @Override
    public SellerResponse saveNewSeller(NewSellerRequest newSeller) {
        if(sellerRepository.existsByContactInfo(newSeller.getContactInfo())) {
          throw new ContactInfoAlreadyExistsException(
                  String.format(
                          "Продавец с контактной информацией %s существует",
                          newSeller.getContactInfo()));
        }
        Seller seller = sellerMapper.newSellerDtoToSeller(newSeller);
        LocalDateTime registrationDate = LocalDateTime.now();
        seller.setRegistrationDate(registrationDate);
        return sellerMapper.sellerToSellerDto(sellerRepository.save(seller));
    }

    @Override
    public SellerResponse updateSeller(UpdatedSellerRequest updatedSeller, Long id) {
        Seller seller = sellerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Не было найдено продавца с id %d", id)));

        if(sellerRepository.existsByContactInfo(updatedSeller.getContactInfo()) && !id.equals(seller.getId())) {
            throw new ContactInfoAlreadyExistsException(String
                    .format("Продавец с контактной информацией %s существует", updatedSeller.getContactInfo()));
        }

        if(updatedSeller.getName() != null) {
            seller.setName(updatedSeller.getName());
        }
        if(updatedSeller.getContactInfo() != null) {
            seller.setContactInfo(updatedSeller.getContactInfo());
        }
        return sellerMapper.sellerToSellerDto(sellerRepository.save(seller));
    }

    @Override
    public void deleteSeller(Long sellerId) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Не было найдено продавца с id %d", sellerId)));
        sellerRepository.delete(seller);
    }
}
