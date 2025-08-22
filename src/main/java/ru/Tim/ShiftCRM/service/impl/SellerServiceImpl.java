package ru.Tim.ShiftCRM.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.Tim.ShiftCRM.repository.SellerRepository;
import ru.Tim.ShiftCRM.service.SellerService;

@RequiredArgsConstructor
@Service
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;

}
