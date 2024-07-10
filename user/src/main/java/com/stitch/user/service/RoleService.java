package com.stitch.user.service;

import com.stitch.user.model.dto.RoleDto;
import com.stitch.user.model.entity.Role;

public interface RoleService {
    RoleDto createUserRole(RoleDto roleDto);

    Role findRoleByName(String name);
}
