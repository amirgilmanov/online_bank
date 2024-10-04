package com.example.online_bank.repository;

import java.util.List;

public interface CustomRepository<T> {
    void save(T t);
    List<T> findAll();
}

