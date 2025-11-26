package com.example.online_bank.domain.model;

import lombok.Data;
import lombok.NonNull;

@Data
public abstract class AbstractBank {
    @NonNull
    protected final String name;
    @NonNull
    protected final String prefixUrl;
}
