package com.example.online_bank.service.user;


import com.example.online_bank.repository.user.UserAuthenticationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


//6. Для аутентификации пользователей нам потребуется сервис аутентификации.
//Аутентификация - проверка для входа в приложение пользователем,
// например ввод логина и пароля на сайте является аутентификацией.
//Сервис будет содержать в себе пары - уникальный индентификатор пользователя - пинкод.
//6.1 У сервиса аутентификации есть метод - добавить новую запись.
// Добавляется пара уникальный индентификатор + пинкод (число из 4 цифр).
//6.2 У сервиса аутентификации есть метод - аутентификация: на вход передается уникальный индентификатор, пинкод.
//На выход передается результат проверки: есть запись с таким уникальным
// индентификатором и пинкод по этому идентификатору совпадает с переданным пинкодом.
//Пример:
//В сервис добавили запись: 'aaa-aaa-aaa-aaaa': 1234.
//Вызывают метод аутентификация с параметрами: 'aaa-aaa-aaa-aaaa', 5552.
// Сервис вернет false, так как по данному уникальному идентификатору пинкод 1234. 1234 не равен 5552.
//Вызывают метод аутентификация с параметрами: 'aaa-aaa-aaa-aaaa', 1234.
// Сервис вернет true, так как по данному уникальному идентификатору хранится пинкод 1234.

@Service
@RequiredArgsConstructor
public class UserAuthenticationService {
    private final UserAuthenticationRepository authenticationRepository;

    public void addNewEntry(String pinCode, String id) {
        authenticationRepository.save(pinCode, id);
    }

    public boolean authentication(String pinCode) {
       return authenticationRepository.findByPinCode(pinCode);
    }
}
