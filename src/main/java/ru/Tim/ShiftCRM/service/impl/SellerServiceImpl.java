package ru.Tim.ShiftCRM.service.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.Tim.ShiftCRM.dto.seller.request.NewSellerDto;
import ru.Tim.ShiftCRM.dto.seller.responce.SellerDto;
import ru.Tim.ShiftCRM.dto.seller.request.UpdatedSellerDto;
import ru.Tim.ShiftCRM.dto.seller.mapper.SellerMapper;
import ru.Tim.ShiftCRM.entity.Seller;
import ru.Tim.ShiftCRM.exception.ContactInfoAlreadyExistsException;
import ru.Tim.ShiftCRM.repository.SellerRepository;
import ru.Tim.ShiftCRM.service.SellerService;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;
    private final SellerMapper sellerMapper;

    @Override
    public Page<SellerDto> getAllSellers(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.ASC, "registrationDate");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Seller> sellers = sellerRepository.findAll(pageable);
        return sellers.map(sellerMapper::sellerToSellerDto);
    }

    @Override
    public SellerDto getSellerInfo(Long id) {
        Seller seller = sellerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Не было найдено продовца с id %d", id)));
        return sellerMapper.sellerToSellerDto(seller);
    }

    @Override
    public Long saveNewSeller(NewSellerDto newSeller) {
        if(sellerRepository.existsByContactInfo(newSeller.getContactInfo())) {
          throw new ContactInfoAlreadyExistsException(
                  String.format(
                          "Продавец с контактной инофрмацией %s существует",
                          newSeller.getContactInfo()));
        }
        Seller seller = sellerMapper.newSellerDtoToSeller(newSeller);
        LocalDateTime registrationDate = LocalDateTime.now();
        seller.setRegistrationDate(registrationDate);
        return sellerRepository.save(seller).getId();
    }

    @Override
    public void updateSeller(UpdatedSellerDto updatedSeller, Long id) {
        if(sellerRepository.existsByContactInfo(updatedSeller.getContactInfo())) {
            throw new ContactInfoAlreadyExistsException(String
                    .format("Продавец с контактной информацией %s существует", updatedSeller.getContactInfo()));
        }
        Seller seller = sellerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Не было найдено продовца с id %d", id)));

        if(updatedSeller.getName() != null) {
            seller.setName(updatedSeller.getName());
        }
        if(updatedSeller.getContactInfo() != null) {
            seller.setContactInfo(updatedSeller.getContactInfo());
        }
        sellerRepository.save(seller);
    }

    @Override
    public void deleteSeller(Long sellerId) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Не было найдено продовца с id %d", sellerId)));
        sellerRepository.delete(seller);
    }
}
