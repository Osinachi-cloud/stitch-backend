package com.stitch.user.service.impl;

import com.stitch.user.exception.UserException;
import com.stitch.user.model.dto.RoleDto;
import com.stitch.user.model.entity.Permission;
import com.stitch.user.model.entity.Role;
import com.stitch.user.repository.PermissionRepository;
import com.stitch.user.repository.RoleRepository;
import com.stitch.user.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static com.stitch.commons.util.Constants.getStr;
import static com.stitch.user.util.DtoMapper.mapRoleToDto;


@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleServiceImpl(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public Role createUserRole(RoleDto roleDto){
        log.debug("Creating customer with request: {}", roleDto);
        Role role = new Role();
        role.setName(roleDto.getName());
        role.setDescription(roleDto.getDescription());

        Collection<Permission> permissions = new ArrayList<>();
        Permission permission = new Permission();
        permission.setName(roleDto.getName());
        permission.setDescription(roleDto.getDescription());
        permission.setCategory(roleDto.getName());
        Permission savedPermission = permissionRepository.save(permission);
        System.err.println(savedPermission);
        permissions.add(savedPermission);
        role.setPermissions(permissions);

        return roleRepository.save(role);

//        return mapRoleToDto(role);
    }

    @Override
    public Optional<Role> findRoleByName(String name){
        System.out.println("in find role method");

        return roleRepository.findRoleByName(getStr(name));



    }
}
