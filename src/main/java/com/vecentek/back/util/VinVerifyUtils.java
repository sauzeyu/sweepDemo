package com.vecentek.back.util;

import java.util.regex.Pattern;

/**
 * @author edgeyu
 * @version 1.0
 * @since 2023/7/13 11:32
 */
public class VinVerifyUtils {

    public static boolean checkVin(String vin) {
        String pattern = "^[a-zA-Z0-9]+$";
        return !Pattern.matches(pattern, vin);
    }
}
