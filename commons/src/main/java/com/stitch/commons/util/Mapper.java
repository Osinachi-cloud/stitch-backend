package com.stitch.commons.util;

import org.springframework.beans.BeanUtils;

import java.util.Objects;

public class Mapper {

    public static Object convertModelToDto(Object source, Object destination){
        BeanUtils.copyProperties(source, destination);
        return destination;
    }


}
