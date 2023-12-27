package com.stitch.commons.util;


import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

public class NumberUtils {
    private static final DecimalFormat twoDecimalPlaceNumberFormat = new DecimalFormat("##0.00");
    private static final DecimalFormat twoDecimalPlaceCurrencyFormat = new DecimalFormat("###,##0.00");
    private static final DecimalFormat zeroDecimalPlaceCurrencyFormat = new DecimalFormat("###,###");
    private static final DecimalFormat numberFormat = new DecimalFormat("######");


    public static String generate(int numOfDigits){
        if(numOfDigits<1) {
            throw new IllegalArgumentException(numOfDigits + ": Number must be equal or greater than 1");
        }
//        long random = (long) Math.floor(Math.random() * 9 * (long)Math.pow(10,numOfDigits-1)) + (long)Math.pow(10,numOfDigits-1);
//        return Long.toString(random);
        return RandomStringUtils.randomNumeric(numOfDigits);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static String formatNumberWithTwoDecimalPlace(double number){

        twoDecimalPlaceNumberFormat.setRoundingMode(RoundingMode.HALF_UP);
        return twoDecimalPlaceNumberFormat.format(number);
    }

    public static String formatCurrencyNumberWithTwoDecimalPlaces(double number){

        twoDecimalPlaceCurrencyFormat.setRoundingMode(RoundingMode.HALF_UP);
        return twoDecimalPlaceCurrencyFormat.format(number);
    }

    public static String formatNumberWithoutDecimalPoint(double number){

        zeroDecimalPlaceCurrencyFormat.setRoundingMode(RoundingMode.HALF_UP);
        return zeroDecimalPlaceCurrencyFormat.format(number);
    }

    public static String formatNumber(double number){

        numberFormat.setRoundingMode(RoundingMode.HALF_UP);
        return numberFormat.format(number);
    }

    public static boolean isNumeric(String number) {
        if(number == null) return false;
        Pattern pattern = Pattern.compile("^\\d+$");
        return pattern.matcher(number).matches();
    }

    public static String formatWithCommas(BigDecimal amount){
        return String.format(Locale.ENGLISH,"%,.2f", amount.setScale(2, RoundingMode.HALF_UP));
    }
}

