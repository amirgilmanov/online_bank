package com.example.online_bank.mapper;

import com.example.online_bank.dto.SignUpDto;
import com.example.online_bank.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "phoneNumber", source = "phone")
    User convertDtoToUser(SignUpDto dto);
}
