package com.example.online_bank.mapper;

import com.example.online_bank.domain.dto.OperationDtoResponse;
import com.example.online_bank.domain.dto.OperationInfoDto;
import com.example.online_bank.domain.entity.Operation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface OperationMapper {
    /**
     * WITHDRAW DTO
     */
    @Mapping(target = "accountNumber", source = "operation.account.accountNumber")
    @Mapping(target = "operationId", source = "id")
    OperationDtoResponse toWithdrawOperationDto(Operation operation);

    /**
     * DEPOSIT DTO
     */
    @Mapping(target = "accountNumber", source = "operation.account.accountNumber")
    @Mapping(target = "operationId", source = "id")
    OperationDtoResponse toDepositOperationDto(Operation operation);

    /**
     * OPERATION INFO DTO
     */
    @Mapping(target = "accountNumber", source = "operation.account.accountNumber")
    OperationInfoDto toOperationInfoDto(Operation operation);
}
