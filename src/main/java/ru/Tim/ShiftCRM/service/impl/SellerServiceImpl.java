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
                .orElseThrow(() -> new EntityNotFoundException("Seller not found"));
        return sellerMapper.sellerToSellerDto(seller);
    }

    @Override
    public void saveNewSeller(NewSellerDto newSeller) {
        Seller seller = sellerMapper.newSellerDtoToSeller(newSeller);
        sellerRepository.save(seller);
    }

    @Override
    public void updateSeller(UpdatedSellerDto updatedSeller, Long id) {
        Seller seller = sellerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Seller for update not found"));

        if(updatedSeller.getName() != null) {
            seller.setName(updatedSeller.getName());
        }
        if(updatedSeller.getContactInfo() != null) {
            seller.setContactInfo(updatedSeller.getContactInfo());
        }
        if(updatedSeller.getRegistrationDate() != null) {
            seller.setRegistrationDate(updatedSeller.getRegistrationDate());
        }
        sellerRepository.save(seller);
    }

    @Override
    public void deleteSeller(Long sellerId) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new EntityNotFoundException("Seller to delete not found"));
        sellerRepository.delete(seller);
    }
}
