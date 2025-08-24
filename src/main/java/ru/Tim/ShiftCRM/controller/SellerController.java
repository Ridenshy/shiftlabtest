package ru.Tim.ShiftCRM.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.Tim.ShiftCRM.dto.seller.NewSellerDto;
import ru.Tim.ShiftCRM.dto.seller.SellerDto;
import ru.Tim.ShiftCRM.dto.seller.UpdatedSellerDto;
import ru.Tim.ShiftCRM.service.SellerService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/apiV1/seller")
public class SellerController {

    private final SellerService sellerService;

    @GetMapping("/getAll")
    public ResponseEntity<Page<SellerDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<SellerDto> sellersPage = sellerService.getAllSellers(page, size);
        return ResponseEntity.ok(sellersPage);
    }

    @GetMapping("/getInfo/{id}")
    public ResponseEntity<SellerDto> getSellerInfo(@PathVariable Long id) {

        SellerDto sellerDto = sellerService.getSellerInfo(id);
        return ResponseEntity.ok(sellerDto);
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createSeller(@RequestBody @Validated NewSellerDto newSellerDto) {
        sellerService.saveNewSeller(newSellerDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Void> updateSeller(
            @PathVariable Long id,
            @RequestBody @Validated UpdatedSellerDto updatedSellerDto) {

        sellerService.updateSeller(updatedSellerDto, id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable Long id) {
        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
    }

}
