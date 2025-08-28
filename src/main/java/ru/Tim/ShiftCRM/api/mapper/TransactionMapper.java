package ru.Tim.ShiftCRM.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.Tim.ShiftCRM.api.model.transaction.request.NewTransactionRequest;
import ru.Tim.ShiftCRM.api.model.transaction.response.TransactionResponse;
import ru.Tim.ShiftCRM.core.entity.Seller;
import ru.Tim.ShiftCRM.core.entity.Transaction;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface TransactionMapper {

    @Mapping(target = "sellerId", expression = "java(transaction.getSeller().getId())")
    TransactionResponse transactionToTransactionDto(Transaction transaction);

    @Mapping(target = "id", ignore = true)
    Transaction newTransactionDtoToTransaction(NewTransactionRequest newTransactionRequest,
                                               Seller seller);

}
