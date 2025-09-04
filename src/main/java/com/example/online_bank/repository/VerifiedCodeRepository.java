package com.example.online_bank.repository;

import com.example.online_bank.domain.entity.VerifiedCode;
import com.example.online_bank.enums.VerifiedCodeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VerifiedCodeRepository extends JpaRepository<VerifiedCode, Long> {
    List<VerifiedCode> findAllByExpiresAtBefore(LocalDateTime now);

    /**
     * Найти неиспользованный код верификации, который еще не истек для конкретного пользователя.
     * Найти код по пользовательскому id и типу кода и код не верифицирован и время истечения БОЛЬШЕ времени, приведенным в параметре
     *
     * @param code        код верификации
     * @param userId      ID пользователя
     * @param type        тип кода (EMAIL, PHONE, RESET)
     * @param currentTime текущее время для проверки
     *
     * @return {@link Optional} с найденным кодом или пустой, если код не найден/истек
     *
     * @implNote Метод ищет коды, которые:
     * <ul>
     *   <li>Соответствуют указанному коду и пользователю</li>
     *   <li>Имеют нужный тип</li>
     *   <li>Еще не были использованы (isVerified = false)</li>
     *  <li>Время истечения больше указанного времени (expires_at > currentTime)</li>
     * </ul>
     * @example т.е. время истечения - 15:00, а время в параметре - 14:30.
     * Время истечение > времени в параметре, значит запись найдется
     * IsAfter - позже
     */
    Optional<VerifiedCode> findByVerifiedCodeAndUser_IdAndCodeTypeAndIsVerifiedIsFalseAndExpiresAtAfter(
            String code, Long userId, VerifiedCodeType type, LocalDateTime currentTime);

    /**
     * Найти все коды, где время истечения раньше времени в параметре
     *
     * @example Время истечения - 15:00, время в параметре - 15:30. Время истечения < времени в параметре, значит
     * код истёк. AtBefore - раньше
     *
     * @param currentTime
     * @param userId
     * @return
     */
    List<VerifiedCode> findAllByExpiresAtBeforeAndUser_Id(LocalDateTime currentTime, Long userId);

    @Modifying
    void deleteAllByIsVerifiedTrueAndUser_id(Long user_id);
}
