package com.example.online_bank.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum TokenType {
    ACCESS("access"), REFRESH("refresh"), ID("id");
    private final String value;
}
