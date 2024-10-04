package com.example.online_bank.repository.user;

import com.example.online_bank.entity.User;
import com.example.online_bank.exception.user_exception.UserException;
import com.example.online_bank.repository.CustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository implements CustomRepository<User> {
    private final Map<String, User> userMap = new HashMap<>();

    @Override
    public void save(User user) {
        userMap.put(user.getId().toString(), user);
    }

    @Override
    public List<User> findAll() {
        return userMap.values().stream().toList();
    }

    public Optional<User> findByNumber(String number) {
        return userMap.values().stream()
                .filter(e -> Objects.equals(e.getPhoneNumber(), number))
                .findAny();
    }

    //5.3 Нахождение пользователя по токену (на вход токен):
//5.3.1 Вытаскиваем из токена пользователя уникальный идентификатор
//5.3.2 Возвращаем пользователя, если пользователь с таким идентификатором существует, иначе выбрасываем ошибку.
    public User findByToken(String token) {
        User user = userMap.values().stream()
                .filter(e -> Objects.equals(e.getToken(), token))
                .findFirst()
                .orElseThrow(() -> new UserException("user with this token not found"));
        boolean b = userMap.containsKey(user.getId().toString());
        if (!b) {
            throw new UserException("user with this id not found");
        } else {
            return user;
        }
    }
}
