package ru.Tim.ShiftCRM.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.Tim.ShiftCRM.api.dto.transaction.request.NewTransactionDto;
import ru.Tim.ShiftCRM.api.dto.transaction.response.TransactionDto;
import ru.Tim.ShiftCRM.core.entity.Seller;
import ru.Tim.ShiftCRM.core.entity.Transaction;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface TransactionMapper {

    @Mapping(target = "sellerId", expression = "java(transaction.getSeller().getId())")
    TransactionDto transactionToTransactionDto(Transaction transaction);

    @Mapping(target = "id", ignore = true)
    Transaction newTransactionDtoToTransaction(NewTransactionDto newTransactionDto,
                                               Seller seller);

}
