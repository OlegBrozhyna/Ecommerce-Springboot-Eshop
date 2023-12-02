package com.eshop.library.service.impl;

import com.eshop.library.dto.AdminDto;
import com.eshop.library.model.Admin;
import com.eshop.library.model.Role;
import com.eshop.library.repository.AdminRepository;
import com.eshop.library.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class AdminServiceImplTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    @Test
    void testSave() {
        // Arrange
        AdminDto adminDto = new AdminDto("admin", "Doe", "john.doe", "12345");

        Role adminRole = new Role("ADMIN");
        when(roleRepository.findByName("ADMIN")).thenReturn(adminRole);

        Admin savedAdmin = new Admin(1L, "admin", "Doe", "john.doe", "12345", Collections.singletonList(adminRole));
        when(adminRepository.save(any(Admin.class))).thenReturn(savedAdmin);

        // Act
        Admin result = adminService.save(adminDto);

    }

    @Test
    void testFindByUsername() {
        // Arrange
        String username = "admin";
        Admin expectedAdmin = new Admin();

        when(adminRepository.findByUsername(username)).thenReturn(expectedAdmin);

        // Act
        Admin result = adminService.findByUsername(username);

        // Assert
        assertEquals(expectedAdmin, result);
    }
}
