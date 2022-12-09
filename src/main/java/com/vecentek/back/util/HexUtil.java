package com.vecentek.back.util;/**
 * @author liujz
 * @date 2022/12/7
 * @apiNote
 */

/**
 * @author liujz
 * @version 1.0
 * @description: TODO
 * @date 2022/12/7 11:14
 */
public class HexUtil {
    public static boolean isAsciiHexString(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (!((c >= '0' && c <= '9') || (c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f'))) {
                return false;
            }
        }
        return true;
    }
}
