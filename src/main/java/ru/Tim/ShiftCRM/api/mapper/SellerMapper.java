package ru.Tim.ShiftCRM.api.mapper;


import org.mapstruct.*;
import ru.Tim.ShiftCRM.api.dto.seller.request.NewSellerDto;
import ru.Tim.ShiftCRM.api.dto.seller.responce.SellerDto;
import ru.Tim.ShiftCRM.core.entity.Seller;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
       )
public interface SellerMapper {

    SellerDto sellerToSellerDto(Seller seller);

    Seller newSellerDtoToSeller(NewSellerDto sellerDto);

}
