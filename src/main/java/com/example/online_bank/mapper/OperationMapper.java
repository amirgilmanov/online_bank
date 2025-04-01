package com.example.online_bank.mapper;

import com.example.online_bank.dto.OperationDtoResponse;
import com.example.online_bank.dto.OperationInfoDtoResponse;
import com.example.online_bank.entity.Operation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

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
    @Mapping(target = "operationId", source = "id")
    @Mapping(target = "amountAfter", source = "operation", qualifiedByName = "calcAmountAfterDeposit")
    @Mapping(target = "amountBefore", source = "operation.account.balance")
    OperationDtoResponse toDepositOperationDto(Operation operation);

    @Mapping(target = "accountNumber", source = "operation.account.accountNumber")
    OperationInfoDtoResponse toOperationInfoDto(Operation operation);

    List<OperationInfoDtoResponse> toOperationInfoDtoList(List<Operation> operations);

    @Named("calcAmountAfterWithdraw")
    default BigDecimal calcAmountAfterWithdraw(Operation operation) {
        return (operation.getAccount().getBalance().subtract(operation.getAmount()));
    }

    @Named("calcAmountAfterDeposit")
    default BigDecimal calcAmountAfterDeposit(Operation operation) {
        return (operation.getAccount().getBalance().add(operation.getAmount()));
    }
}
