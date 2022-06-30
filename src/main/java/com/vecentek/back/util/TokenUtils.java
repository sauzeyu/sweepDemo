package com.vecentek.auth.util;

import cn.hutool.core.lang.UUID;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vecentek.auth.constant.JwtConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-02-25 13:50
 */


public class TokenUtils {

    /**
     * value 不能为空
     *
     * @param key   token 载荷中的 key
     * @param value token 载荷中的 value
     * @return token
     */
    public static <T> String generateToken(String key, T value) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        if (value == null) {
            return "";
        }
        // 计算超时时间 默认设置1天
        ZonedDateTime zdt = LocalDate.now().plus(1, ChronoUnit.DAYS).atStartOfDay(ZoneId.systemDefault());
        Date expireDate = Date.from(zdt.toInstant());

        Object obj = value;
        if (value.getClass() != String.class) {
            obj = new ObjectMapper().writeValueAsString(value);
        }
        return Jwts.builder()
                // jwt payload --> kv
                .claim(key, obj)
                .setExpiration(expireDate)
                // jwt id
                .setId(UUID.randomUUID().toString())
                // jwt签名 --> 加密
                .signWith(getPrivateKey(), SignatureAlgorithm.RS256)
                .compact();

    }


    /**
     * @param key   解析 token 需要的key
     * @param token token
     * @param clazz token 中 value 的类对象
     * @param <T>   value 反序类化后的对象
     * @return value 反序类化后的对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T parseToken(String key, String token, Class<T> clazz) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJws(token);
        String body = claimsJws.getBody().get(key).toString();
        if (clazz == String.class) {
            return (T) body;
        }
        return new ObjectMapper().readValue(body, clazz);
    }


    /**
     * 生成公钥
     *
     * @return 公钥
     */
    private static PublicKey getPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(
                new BASE64Decoder().decodeBuffer(JwtConstant.PUBLIC_KEY)
        );
        return KeyFactory.getInstance("RSA").generatePublic(keySpec);
    }

    /**
     * 生成私钥
     *
     * @return 私钥
     */
    private static PrivateKey getPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
                new BASE64Decoder().decodeBuffer(JwtConstant.PRIVATE_KEY)
        );
        return KeyFactory.getInstance("RSA").generatePrivate(pkcs8EncodedKeySpec);
    }

}
