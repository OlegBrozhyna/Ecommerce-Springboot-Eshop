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

    // Mocking the AdminRepository and RoleRepository for testing purposes.
    @Mock
    private AdminRepository adminRepository;

    @Mock
    private RoleRepository roleRepository;

    // Injecting mocks into the AdminServiceImpl for testing.
    @InjectMocks
    private AdminServiceImpl adminService;

    // Testing the save method of AdminServiceImpl.
    @Test
    void testSave() {
        // Arrange
        // Creating a sample AdminDto for testing.
        AdminDto adminDto = new AdminDto("admin", "Doe", "john.doe", "12345");

        // Creating a sample admin role and configuring the behavior of roleRepository.
        Role adminRole = new Role("ADMIN");
        when(roleRepository.findByName("ADMIN")).thenReturn(adminRole);

        // Creating a sample savedAdmin and configuring the behavior of adminRepository.
        Admin savedAdmin = new Admin(1L, "admin", "Doe", "john.doe", "12345", Collections.singletonList(adminRole));
        when(adminRepository.save(any(Admin.class))).thenReturn(savedAdmin);

        // Act
        // Invoking the save method and capturing the result.
        Admin result = adminService.save(adminDto);

        // Assert
        // Verifying that the result matches the expected savedAdmin.
        assertEquals(savedAdmin, result);
    }

    // Testing the findByUsername method of AdminServiceImpl.
    @Test
    void testFindByUsername() {
        // Arrange
        // Providing a sample username for testing.
        String username = "admin";

        // Creating an expectedAdmin and configuring the behavior of adminRepository.
        Admin expectedAdmin = new Admin();
        when(adminRepository.findByUsername(username)).thenReturn(expectedAdmin);

        // Act
        // Invoking the findByUsername method and capturing the result.
        Admin result = adminService.findByUsername(username);

        // Assert
        // Verifying that the result matches the expectedAdmin.
        assertEquals(expectedAdmin, result);
    }
}