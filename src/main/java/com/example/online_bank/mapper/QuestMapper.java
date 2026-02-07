package com.example.online_bank.mapper;

import com.example.online_bank.domain.dto.QuestResponseDto;
import com.example.online_bank.domain.entity.Quest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuestMapper {
    QuestResponseDto toDto(Quest quest);
}
