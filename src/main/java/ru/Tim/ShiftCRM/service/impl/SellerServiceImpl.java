package ru.Tim.ShiftCRM.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.Tim.ShiftCRM.dto.seller.NewSellerDto;
import ru.Tim.ShiftCRM.dto.seller.SellerDto;
import ru.Tim.ShiftCRM.dto.seller.UpdatedSellerDto;
import ru.Tim.ShiftCRM.dto.seller.mapper.SellerMapper;
import ru.Tim.ShiftCRM.entity.Seller;
import ru.Tim.ShiftCRM.repository.SellerRepository;
import ru.Tim.ShiftCRM.service.SellerService;

import java.awt.*;
import java.time.LocalDate;
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
                        String.format("No seller found with id: %d", id)));
        return sellerMapper.sellerToSellerDto(seller);
    }

    @Override
    public void saveNewSeller(NewSellerDto newSeller) {
        Seller seller = sellerMapper.newSellerDtoToSeller(newSeller);
        LocalDateTime registrationDate = LocalDateTime.now();
        seller.setRegistrationDate(registrationDate);
        sellerRepository.save(seller);
    }

    @Override
    public void updateSeller(UpdatedSellerDto updatedSeller, Long id) {
        Seller seller = sellerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("No Seller with id: %d for update", id)));

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
                        String.format("No Seller with id: %d for delete", sellerId)));
        sellerRepository.delete(seller);
    }
}
