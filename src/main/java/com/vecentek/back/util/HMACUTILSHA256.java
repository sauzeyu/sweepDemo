package com.vecentek.back.util;

import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import com.payneteasy.tlv.HexUtil;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HMACUTILSHA256 {
    /**
     * 将加密后的字节数组转换成字符串
     *
     * @param b 字节数组
     * @return 字符串
     */

    public static String byteArrayToHexString(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toLowerCase();
    }

    /**
     * sha256_HMAC加密
     * @return 加密后字符串
     */

    public static String sha256_HMAC(String message,String secret) {
        String hash = "";
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] bytes = sha256_HMAC.doFinal(message.getBytes());
            hash = byteArrayToHexString(bytes);
        } catch (Exception e) {
            System.out.println("Error HmacSHA256 ===========" + e.getMessage());
        }
        return hash;
    }

    public static String sha256_HMAC( byte[] message,  byte[] secret) {
        String hmacByte = "";
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret, "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] bytes = sha256_HMAC.doFinal(message);
            hmacByte=HexUtil.toHexString(bytes);
//            hash = byteArrayToHexString(bytes);
        } catch (Exception e) {
            System.out.println("Error HmacSHA256 ===========" + e.getMessage());
        }
        return hmacByte;
    }

    public static byte[] sha256_HMAC(byte[] message, String secret) {
        byte[] bytes=null;
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(HexUtil.parseHex(secret), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            bytes = sha256_HMAC.doFinal(message);
        } catch (Exception e) {
            System.out.println("Error HmacSHA256 ===========" + e.getMessage());
        }
        return bytes;
    }

}
