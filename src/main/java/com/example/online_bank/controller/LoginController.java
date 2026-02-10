package com.example.online_bank.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    //для того чтобы показать что рефреш токен истек и потребуется вход с паролем. т.к. устройство есть в списке доверенных,
    //а если бы его там не было то пр попытке входа с паролем и почтой пришлось бы сделать подтверждение владения почтой т.е.
    //входим - получаем данные с устройства
    //устройство есть а списке доверенных для пользователя с конкретным емайл?
    //если есть, то окей входим
    //если нет, то отправляем ему код на почту т.к. произошел вход с нового устройства
    @PostMapping
    public void login(){
        //фронт отправляет рефреш токен:
        // рефреш токен который есть в бд становится не валидным, RefreshTokenService - создает новый refresh
        //этот токен
    }
}
