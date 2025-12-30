package com.example.online_bank.mapper;

import com.example.online_bank.domain.dto.RegistrationDto;
import com.example.online_bank.domain.event.UserRegisterEvent;
import com.example.online_bank.domain.dto.UserContainer;
import com.example.online_bank.domain.entity.Role;
import com.example.online_bank.domain.entity.User;
import com.example.online_bank.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Slf4j
public class UserMapperTest {
    private final UserMapperImpl userMapper = new UserMapperImpl();
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private final RoleService roleService = Mockito.mock(RoleService.class);
    private RegistrationDto registrationDto;
    private User user;
    private String uuidStr;

    @BeforeEach
    void setUp() {
        registrationDto = new RegistrationDto(
                "Test",
                "TestSurname",
                "TestPatronymic",
                "89999999999",
                "pass",
                "testEmail@.com"
        );

        UUID uuid = UUID.randomUUID();
        user = User.builder()
                .name("Test")
                .uuid(uuid)
                .roles(List.of(new Role(1L, "ROLE_USER")))
                .build();
        uuidStr = uuid.toString();
    }

    @Test
    @DisplayName("Успешное конвертирование в UserContainer")
    void successfulMapToUserContainer() {
        UserContainer userContainer = userMapper.toUserContainer(user);

        assertEquals(uuidStr, userContainer.uuid());
        assertNotNull(userContainer);
        assertEquals("Test", userContainer.name());
        assertArrayEquals(List.of("ROLE_USER").toArray(), userContainer.roles().toArray());
    }

    @Test
    @DisplayName("Успешное конвертирование в пользователя во время регистрации")
    void successfulMapToUser() {
        //Подготовка данных

        List<Role> roles = List.of(new Role(1L, "ROLE_USER"));
        Mockito.when(roleService.findRoleByName("ROLE_USER")).thenReturn(new Role(1L, "ROLE_USER"));

        //Сверяем данные
        User user = userMapper.toUser(registrationDto, roleService, bCryptPasswordEncoder);
        log.info(user.toString());
        assertNotNull(user);
        assertNotNull(user.getUuid());
        assertEquals("Test", user.getName());
        assertEquals("TestSurname", user.getSurname());
        assertEquals("TestPatronymic", user.getPatronymic());
        assertEquals("89999999999", user.getPhoneNumber());
        assertEquals("testEmail@.com", user.getEmail());
        assertEquals(0, user.getFailedAttempts());
        assertFalse(user.getIsBlocked());
        assertFalse(user.getIsVerified());
        assertArrayEquals(roles.toArray(), user.getRoles().toArray());
    }

    @Test
    void successMapToUserAdmin() {
        List<Role> roles = List.of(new Role(1L, "ROLE_ADMIN"));
        Mockito.when(roleService.findRoleByName("ROLE_ADMIN")).thenReturn(new Role(1L, "ROLE_ADMIN"));

        User userAdmin = userMapper.toUserAdmin(registrationDto, roleService, bCryptPasswordEncoder);
        assertNotNull(userAdmin);
        assertNotNull(userAdmin.getUuid());
        assertEquals("Test", userAdmin.getName());
        assertEquals("TestSurname", userAdmin.getSurname());
        assertEquals("TestPatronymic", userAdmin.getPatronymic());
        assertEquals("89999999999", userAdmin.getPhoneNumber());
        assertEquals("testEmail@.com", userAdmin.getEmail());
        assertEquals(0, userAdmin.getFailedAttempts());
        assertFalse(userAdmin.getIsBlocked());
        assertFalse(userAdmin.getIsVerified());
        assertArrayEquals(roles.toArray(), userAdmin.getRoles().toArray());
    }

    @Test
    @DisplayName("Успешное конвертирование в registrationDtoResponse")
    void successfulMapToRegistrationDtoResponse() {
        String code = "1234";
        UserRegisterEvent userRegisterEvent = userMapper.toRegistrationDtoResponse(registrationDto, code);

        assertEquals(code, userRegisterEvent.code());
        assertEquals("testEmail@.com", userRegisterEvent.email());
    }
}
