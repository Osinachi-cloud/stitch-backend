package com.stitch.commons.util;


import org.apache.commons.lang3.RandomStringUtils;



public class AlphaNumericUtils {


    public static String generate(int numOfChars){
        if(numOfChars<1) {
            throw new IllegalArgumentException(numOfChars + ": Number of characters must be equal or greater than 1");
        }
        return RandomStringUtils.randomAlphanumeric(10);
    }


}

