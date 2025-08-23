package ru.Tim.ShiftCRM.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.Tim.ShiftCRM.entity.Seller;
import ru.Tim.ShiftCRM.service.SellerService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/apiV1/seller")
public class SellerController {

    private final SellerService sellerService;



}
