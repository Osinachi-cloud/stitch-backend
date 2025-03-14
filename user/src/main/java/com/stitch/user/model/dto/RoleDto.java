package com.stitch.user.model.dto;

import com.stitch.user.enums.RoleName;
import com.stitch.user.model.entity.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.stitch.user.util.DtoMapper.mapToCollectionOfPermissionDto;

@Data
@NoArgsConstructor
public class RoleDto {

    private String name;

    private String description;

    private String dateCreated;

    private String lastUpdated;

    private List<String> permissionNames = new ArrayList<>();

    private Collection<PermissionDto> permissionsDto;

    public RoleDto(Role role) {
        this.name = role.getName();
        this.description = role.getDescription();
        this.dateCreated = role.getDateCreated().toString();
        this.lastUpdated = role.getLastUpdated().toString();
        this.permissionsDto = mapToCollectionOfPermissionDto(role.getPermissions());
    }
}