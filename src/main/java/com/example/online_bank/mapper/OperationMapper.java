package com.example.online_bank.mapper;

import com.example.online_bank.domain.dto.OperationDtoResponse;
import com.example.online_bank.domain.dto.OperationInfoDto;
import com.example.online_bank.domain.entity.Operation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.util.List;


@Mapper(componentModel = "spring")
public interface OperationMapper {

    @Mapping(target = "accountNumber", source = "operation.account.accountNumber")
    @Mapping(target = "operationId", source = "id")
    @Mapping(target = "amountAfter", source = "operation", qualifiedByName = "calcAmountAfterWithdraw")
    @Mapping(target = "amountBefore", source = "operation.account.balance")
    OperationDtoResponse toWithdrawOperationDto(Operation operation);

    @Mapping(target = "accountNumber", source = "operation.account.accountNumber")
    @Mapping(target = "operationId", source = "operation.id")
    @Mapping(target = "amountAfter", source = "operation.account.balance")
    @Mapping(target = "amountBefore", source = "balanceBefore")
    OperationDtoResponse toDepositOperationDto(Operation operation, BigDecimal balanceBefore);

    @Mapping(target = "accountNumber", source = "operation.account.accountNumber")
    OperationInfoDto toOperationInfoDto(Operation operation);

    List<OperationInfoDto> toOperationInfoDtoList(List<Operation> operations);

    @Named("calcAmountAfterWithdraw")
    default BigDecimal calcAmountAfterWithdraw(Operation operation) {
        return (operation.getAccount().getBalance().subtract(operation.getAmount()));
    }
}
