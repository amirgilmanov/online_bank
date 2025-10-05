package com.example.online_bank.mapper;

import com.example.online_bank.domain.dto.RegistrationDto;
import com.example.online_bank.domain.dto.UserDetails;
import com.example.online_bank.domain.entity.Role;
import com.example.online_bank.domain.entity.User;
import com.example.online_bank.service.RoleService;
import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.lang.Boolean.FALSE;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "uuid", source = "user.uuid", qualifiedByName = "uuidToString")
    @Mapping(target = "roles", source = "user.roles", qualifiedByName = "rolesToString")
    UserDetails toUserDetails(User user);

    @Mapping(target = "phoneNumber", source = "phone")
    @Mapping(target = "passwordHash", source = "password", qualifiedByName = "encodePassword")
    User toUser(RegistrationDto dto, @Context BCryptPasswordEncoder passwordEncoder, @Context RoleService roleService);

    @Named("rolesToString")
    default Set<String> rolesToString(List<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }

    @Named("uuidToString")
    default String uuidToString(UUID uuid) {
        return uuid.toString();
    }

    @Named("encodePassword")
    default String encodePassword(@Context BCryptPasswordEncoder encoder, String password) {
        return encoder.encode(password);
    }

    @AfterMapping
    default void assignDefaultsRoles(
            @MappingTarget User user,
            @Context RoleService roleService
    ) {
        String roleName = "ROLE_USER";
        List<Role> roles = List.of(roleService.findRoleByName(roleName)
                .orElseThrow(() -> new EntityNotFoundException("Роль %s не найдена".formatted(roleName))));
        assignUuid(user);
        assignFailedAttempts(user);
        assignIsBlocked(user);
        assignRoles(user, roles);
        assignIsVerified(user);
    }

    default void assignUuid(User user) {
        user.setUuid(UUID.randomUUID());
    }

    default void assignFailedAttempts(User user) {
        user.setFailedAttempts(Integer.valueOf(0));
    }

    default void assignIsBlocked(User user) {
        user.setIsBlocked(FALSE);
    }

    default void assignRoles(User user, List<Role> roles) {
        user.setRoles(roles);
    }

    default void assignIsVerified(User user) {
        user.setIsVerified(FALSE);
    }
}
