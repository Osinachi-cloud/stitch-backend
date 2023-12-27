package com.stitch.commons.model.dto;

import lombok.Data;

@Data
public class PaginatedResponse <T> {
    private int page;
    private int size;
    private int total;
    private T data;
}
