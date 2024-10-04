package com.example.online_bank.service.user;

import com.example.online_bank.dto.user_dto.UserDtoRequest;
import com.example.online_bank.entity.User;
import com.example.online_bank.exception.user_exception.UserAuthenticationException;
import com.example.online_bank.exception.user_exception.UserException;
import com.example.online_bank.repository.user.UserRepository;
import com.example.online_bank.service.other.CodeGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

//5.1. Регистрация пользователя: передается телефон, фио владельца.
// Возврашает случайный пинкод из 4 цифр (ТЕКСТ! "0001").
//5.1.0. Проверяет что пользователя с таким телефоном нет, иначе ошибка.
//5.1.1. Создает пользователя с телефоном, фио, случайно сгенерированным UUID (UUID.randomUUID()).
// Генерируется случайный пинкод.
//5.1.2. Заносится в сервис аутентификации новая запись (6.1)
//5.1.3. Возвращает сгенерированный пинкод.
//5.1.4. Созданный пользователь хранится в репозитории для пользователей.

@Service
@RequiredArgsConstructor
public class UserRegistrationService {
    private final UserRepository userRepository;
    private final UserAuthenticationService authenticationService;
    private final CodeGeneratorService codeGeneratorService;
  //  private final Random random;

    public String registration(UserDtoRequest userDto) {
        Optional<User> existingUser = userRepository.findByNumber(userDto.phone());
        if (existingUser.isPresent()) {
            throw new UserException("пользователь с таким номером уже зарегистрирован");
        }
        User user = convertDtoToUser(userDto);
        String pinCode = codeGeneratorService.generatePinCode();
        String userId = user.getId().toString();
        user.setPinCode(pinCode);
        userRepository.save(user);
        authenticationService.addNewEntry(pinCode, userId);
        return pinCode;
    }

    public String authenticationUser(String phoneNumber, String pinCode) {
        //TODO : удалять пробелы в пинкоде
        User existingUser = userRepository.findByNumber(phoneNumber).orElseThrow();
        boolean authentication = authenticationService.authentication(pinCode);
        if (!authentication) {
            throw new UserAuthenticationException("неверный пин-код");
        } else  {
            String userId = existingUser.getId().toString();
            String token = String.format("online%stoken", userId);
            existingUser.setToken(token);
        }
        return existingUser.getToken();
    }

    public User findByToken(String token) {
        return userRepository.findByToken(token);
    }

    @SuppressWarnings("checkstyle:MagicNumber")
//    private String getRandomNumber() {
//        return String.format("%04d", random.nextInt(10_000));
//    }

    //TODO: сделать маппер в будущем
    private User convertDtoToUser(UserDtoRequest dto) {
        return User.builder()
                .id(UUID.randomUUID())
                .name(dto.name())
                .surname(dto.surname())
                .patronymic(dto.patronymic())
                .phoneNumber(dto.phone())
                .build();
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }
}
