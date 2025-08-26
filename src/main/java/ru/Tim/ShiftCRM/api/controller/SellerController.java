package ru.Tim.ShiftCRM.api.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.Tim.ShiftCRM.api.swagger.OpenApi.SellerApi;
import ru.Tim.ShiftCRM.api.dto.seller.request.NewSellerDto;
import ru.Tim.ShiftCRM.api.dto.seller.responce.SellerDto;
import ru.Tim.ShiftCRM.api.dto.seller.request.UpdatedSellerDto;
import ru.Tim.ShiftCRM.core.service.SellerService;


@RequiredArgsConstructor
@RestController
@RequestMapping("/apiV1/seller")
@Validated
public class SellerController implements SellerApi {

    private final SellerService sellerService;

    @Override
    @GetMapping("/getAll")
    public ResponseEntity<Page<SellerDto>> getAll(
            @RequestParam(defaultValue = "0")
            @PositiveOrZero int page,
            @RequestParam(defaultValue = "50")
            @Positive int size) {
        Page<SellerDto> sellersPage = sellerService.getAllSellers(page, size);
        return ResponseEntity.ok(sellersPage);
    }

    @Override
    @GetMapping("/getInfo/{id}")
    public ResponseEntity<SellerDto> getSellerInfo(@PathVariable @NotNull Long id) {

        SellerDto sellerDto = sellerService.getSellerInfo(id);
        return ResponseEntity.ok(sellerDto);
    }

    @Override
    @PostMapping("/create")
    public ResponseEntity<String> createSeller(@RequestBody @Validated NewSellerDto newSellerDto) {
        Long id = sellerService.saveNewSeller(newSellerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(String.format("Продацец с id %d создан", id));
    }

    @Override
    @PatchMapping("/update/{id}")
    public ResponseEntity<String> updateSeller(
            @PathVariable Long id,
            @RequestBody @Validated UpdatedSellerDto updatedSellerDto) {

        sellerService.updateSeller(updatedSellerDto, id);
        return ResponseEntity.ok(String.format("Продавец с id %d обновлен", id));
    }

    @Override
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteSeller(@PathVariable Long id) {
        sellerService.deleteSeller(id);
        return ResponseEntity.ok(String.format("Продавец с id %d удален", id));
    }

}
