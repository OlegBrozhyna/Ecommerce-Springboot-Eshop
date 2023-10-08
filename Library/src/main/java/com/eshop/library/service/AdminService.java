package com.eshop.library.service;

import com.eshop.library.dto.AdminDto;
import com.eshop.library.model.Admin;

public interface AdminService {
    Admin findByUsername(String username);

    Admin save(AdminDto adminDto);
}
