package ru.Tim.ShiftCRM.dto.transaction.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.Tim.ShiftCRM.dto.transaction.NewTransactionDto;
import ru.Tim.ShiftCRM.dto.transaction.TransactionDto;
import ru.Tim.ShiftCRM.entity.Seller;
import ru.Tim.ShiftCRM.entity.Transaction;

@Mapper(
        //unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface TransactionMapper {

    @Mapping(target = "sellerId", expression = "java(transaction.getSeller().getId())")
    TransactionDto transactionToTransactionDto(Transaction transaction);


    Transaction newTransactionDtoToTransaction(NewTransactionDto newTransactionDto,
                                               Seller seller);

}
