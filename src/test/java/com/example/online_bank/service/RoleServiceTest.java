package com.example.online_bank.service;

import com.example.online_bank.domain.entity.Role;
import com.example.online_bank.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @InjectMocks
    private RoleService roleService;
    @Mock
    private RoleRepository roleRepository;

    @Test
    void successFindRole() {
        Role roleUser = Role.builder()
                .id(1L)
                .name("ROLE_USER")
                .build();
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(roleUser));
        Role result = roleService.findRoleByName("ROLE_USER");

        Assertions.assertDoesNotThrow(() -> roleService.findRoleByName("ROLE_USER"));
        Assertions.assertEquals("ROLE_USER", result.getName());
    }

    @Test
    void failerFindRole_RoleNameNotFound() {
        when(roleRepository.findByName("SOME_NAME")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> roleService.findRoleByName("SOME_NAME"));
    }
}