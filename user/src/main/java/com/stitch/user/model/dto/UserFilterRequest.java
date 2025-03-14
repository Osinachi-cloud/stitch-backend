package com.stitch.user.model.dto;

import lombok.Data;

@Data
public class UserFilterRequest {
    private int page;
    private int size;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private Long roleId;
}
