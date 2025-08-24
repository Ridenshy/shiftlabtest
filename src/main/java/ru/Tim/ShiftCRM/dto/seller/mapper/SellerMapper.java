package ru.Tim.ShiftCRM.dto.seller.mapper;


import org.mapstruct.*;
import ru.Tim.ShiftCRM.dto.seller.request.NewSellerDto;
import ru.Tim.ShiftCRM.dto.seller.responce.SellerDto;
import ru.Tim.ShiftCRM.dto.seller.request.UpdatedSellerDto;
import ru.Tim.ShiftCRM.entity.Seller;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
       )
public interface SellerMapper {

    SellerDto sellerToSellerDto(Seller seller);

    Seller newSellerDtoToSeller(NewSellerDto sellerDto);

    Seller updatedSellerDtoToSeller(UpdatedSellerDto updatedSellerDto);

}
