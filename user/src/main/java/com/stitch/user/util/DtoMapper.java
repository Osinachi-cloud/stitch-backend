package com.stitch.user.util;

import com.stitch.user.model.dto.BodyMeasurementDto;
import com.stitch.user.model.dto.BodyMeasurementRequest;
import com.stitch.user.model.dto.PermissionDto;
import com.stitch.user.model.dto.RoleDto;
import com.stitch.user.model.entity.BodyMeasurement;
import com.stitch.user.model.entity.Permission;
import com.stitch.user.model.entity.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public class DtoMapper {

    public static BodyMeasurement bodyMeasurementRequestToEntity(BodyMeasurementRequest bodyMeasurementRequest){
        BodyMeasurement bodyMeasurement = new BodyMeasurement();
        BeanUtils.copyProperties(bodyMeasurementRequest, bodyMeasurement);
        return bodyMeasurement;
    }

    public static BodyMeasurementDto bodyMeasurementEntityToDto(BodyMeasurement bodyMeasurement){
        BodyMeasurementDto bodyMeasurementDto = new BodyMeasurementDto();
        BeanUtils.copyProperties(bodyMeasurement, bodyMeasurementDto);
        return bodyMeasurementDto;
    }

    public static RoleDto mapRoleToDto(Role role) {
        log.info("role permission: {}", role.getPermissions());
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }

        RoleDto roleDto = new RoleDto();
        roleDto.setName(role.getName());
        roleDto.setDescription(role.getDescription());
        roleDto.setDateCreated(role.getDateCreated().toString());
        roleDto.setLastUpdated(role.getLastUpdated().toString());
        roleDto.setPermissionNames(mapToCollectionNamesToString(role.getPermissions()));
        roleDto.setPermissionsDto(mapToCollectionOfPermissionDto(role.getPermissions()));
        return roleDto;
    }

    public static List<String> mapToCollectionNamesToString(Collection<Permission> permissionCollection) {
        List<String> permissionNames = new ArrayList<>();
        for (Permission permission : permissionCollection) {
            if (permission != null) {
                String permissionName = permission.getName();
                permissionNames.add(permissionName);
            }
        }
        return permissionNames;
    }

    public static Collection<PermissionDto> mapToCollectionOfPermissionDto(Collection<Permission> permissionCollection) {
        Collection<PermissionDto> permissionDtos = new ArrayList<>();
        for (Permission permission : permissionCollection) {
            if (permission != null) {
                PermissionDto permissionDto = new PermissionDto();
                permissionDto.setName(permission.getName());
                permissionDto.setId(permission.getId());
                permissionDto.setDescription(permission.getDescription());
                permissionDto.setCategory(permission.getCategory());
                permissionDtos.add(permissionDto);
            }
        }
        return permissionDtos;
    }

    public static RoleDto mapRoleToDtoLoginResponse(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
        RoleDto roleDto = new RoleDto();
        roleDto.setName(role.getName());
        roleDto.setDescription(role.getDescription());
//        roleDto.setPermissionsDto(mapToCollectionOfPermissionDto(role.getPermissions()));
        System.out.println("=========== permissions");
        System.out.println(role.getPermissions().size());

        roleDto.setPermissionNames(mapToCollectionOfPermissionDtoLoginResponse(role.getPermissions()));
        return roleDto;
    }

    public static List<String> mapToCollectionOfPermissionDtoLoginResponse(Collection<Permission> permissionCollection) {
        List<String> stringList = new ArrayList<>();

//        log.info("permissionCollection : {}", permissionCollection);
        for (Permission permission : permissionCollection) {
            log.info("permission_names : {}", permission.getName());
            if (permission != null) {
//                PermissionDto permissionDto = new PermissionDto();
//                permissionDto.setName(permission.getName());
//                permissionDto.setId(permission.getId());
//                permissionDto.setDescription(permission.getDescription());
                String name = permission.getName();
                stringList.add(name);
            }
        }
        System.out.println("================= string list");
        System.out.println(stringList);
        return stringList;
    }
}
