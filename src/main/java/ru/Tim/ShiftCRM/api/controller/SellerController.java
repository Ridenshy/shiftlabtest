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
import ru.Tim.ShiftCRM.api.model.seller.responce.SellerResponse;
import ru.Tim.ShiftCRM.api.swagger.OpenApi.SellerApi;
import ru.Tim.ShiftCRM.api.model.seller.request.NewSellerRequest;
import ru.Tim.ShiftCRM.api.model.seller.request.UpdatedSellerRequest;
import ru.Tim.ShiftCRM.core.service.SellerService;


@RequiredArgsConstructor
@RestController
@RequestMapping("/apiV1/sellers")
@Validated
public class SellerController implements SellerApi {

    private final SellerService sellerService;

    @Override
    @GetMapping()
    public ResponseEntity<Page<SellerResponse>> getAll(
            @RequestParam(defaultValue = "0")
            @PositiveOrZero(message = "Параметр page должен быть больше или равен 0")
            int page,
            @RequestParam(defaultValue = "50")
            @Positive(message = "Параметр size должен быть больше 0")
            int size
    ) {
        Page<SellerResponse> sellersPage = sellerService.getAllSellers(page, size);
        return ResponseEntity.ok(sellersPage);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<SellerResponse> getSellerInfo(
            @PathVariable
            @NotNull(message = "Переменная пути id не должна быть Null")
            Long id
    ) {
        SellerResponse sellerResponse = sellerService.getSellerInfo(id);
        return ResponseEntity.ok(sellerResponse);
    }

    @Override
    @PostMapping()
    public ResponseEntity<SellerResponse> createSeller(
            @RequestBody
            @Validated
            NewSellerRequest newSellerRequest
    ) {
        SellerResponse sellerResponse = sellerService.saveNewSeller(newSellerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(sellerResponse);
    }

    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<SellerResponse> updateSeller(
            @PathVariable
            @NotNull(message = "Переменная пути id не должна быть Null")
            Long id,
            @RequestBody
            @Validated
            UpdatedSellerRequest updatedSellerRequest
    ) {
        SellerResponse sellerResponse =  sellerService.updateSeller(updatedSellerRequest, id);
        return ResponseEntity.ok(sellerResponse);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSeller(@PathVariable Long id) {
        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
    }

}
