package com.stitch.commons.util;

import com.stitch.commons.model.dto.Response;

public class ResponseUtils {

    public static Response createDefaultSuccessResponse(){
        return new Response(0, "Successful");
    }

    public static Response createResponse(int code, String message){
        return new Response(code, message);
    }

    public static Response createSuccessResponse(String message){
        return new Response(0, message);
    }
}
