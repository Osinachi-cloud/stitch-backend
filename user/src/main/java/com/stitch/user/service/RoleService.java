package com.stitch.user.service;

import com.stitch.user.model.dto.RoleDto;
import com.stitch.user.model.entity.Role;

import java.util.Optional;

public interface RoleService {
    Role createUserRole(RoleDto roleDto);

    Optional<Role> findRoleByName(String name);
}
