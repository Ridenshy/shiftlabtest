package ru.Tim.ShiftCRM.api.mapper;


import org.mapstruct.*;
import ru.Tim.ShiftCRM.api.model.seller.request.NewSellerRequest;
import ru.Tim.ShiftCRM.api.model.seller.responce.SellerResponse;
import ru.Tim.ShiftCRM.core.entity.Seller;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
       )
public interface SellerMapper {

    SellerResponse sellerToSellerDto(Seller seller);

    Seller newSellerDtoToSeller(NewSellerRequest sellerDto);

}
