package com.example.online_bank.mapper;

import com.example.online_bank.dto.AccountDtoResponse;
import com.example.online_bank.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "holderName", source = "account.holder.name")
    @Mapping(target = "holderSurname", source = "account.holder.surname")
    AccountDtoResponse toDtoResponse(Account account);
}
