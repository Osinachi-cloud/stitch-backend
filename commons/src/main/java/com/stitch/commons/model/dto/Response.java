package com.stitch.commons.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@AllArgsConstructor
//@NoArgsConstructor
public class Response {

    private int code;
    private String message;

    public Response(){

    }

    public Response(int code, String message){
        this.code = code;
        this.message = message;
    }

}
