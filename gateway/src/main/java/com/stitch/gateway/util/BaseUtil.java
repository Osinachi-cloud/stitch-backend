package com.stitch.gateway.util;

import java.util.Objects;

import static com.stitch.gateway.util.Constants.EMPTY_STRING;

public class BaseUtil {

    public static String getString(String value){
        if (Objects.isNull(value)){
            return EMPTY_STRING;
        }
        return value.trim();
    }
}
