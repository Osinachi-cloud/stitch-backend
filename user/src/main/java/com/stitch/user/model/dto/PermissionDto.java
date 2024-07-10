package com.stitch.user.model.dto;

import com.stitch.user.model.entity.Permission;
import lombok.Data;

@Data
public class PermissionDto {
    private Long id;

    private String name;

    private String description;

    private String category;
    public PermissionDto(){}
    public PermissionDto(Permission permission){
        this.name = permission.getName();
        this.description = permission.getDescription();
    }

}