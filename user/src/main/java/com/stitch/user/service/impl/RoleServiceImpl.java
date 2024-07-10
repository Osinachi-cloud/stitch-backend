package com.stitch.user.service.impl;

import com.stitch.user.exception.UserException;
import com.stitch.user.model.dto.PermissionDto;
import com.stitch.user.model.dto.RoleDto;
import com.stitch.user.model.entity.Permission;
import com.stitch.user.model.entity.Role;
import com.stitch.user.repository.RoleRepository;
import com.stitch.user.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static com.stitch.user.util.DtoMapper.mapRoleToDto;


@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public RoleDto createUserRole(RoleDto roleDto){
        log.debug("Creating customer with request: {}", roleDto);
        Role role = new Role();
        role.setName(roleDto.getName());
        role.setDescription(roleDto.getDescription());

        Collection<Permission> permissions = new ArrayList<>();
        Permission permission = new Permission();
        permission.setName(roleDto.getName());
        permissions.add(permission);
        role.setPermissions(permissions);

        return mapRoleToDto(role);
    }

    @Override
    public Role findRoleByName(String name){

        Optional<Role> optionalRole = roleRepository.findRoleByName(name);
        if(optionalRole.isEmpty()){
            throw new UserException("Role not found");
        }
        log.info("optionalRole : {}", optionalRole.get().getPermissions());
        return optionalRole.get();
    }
}
