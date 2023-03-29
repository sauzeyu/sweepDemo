package com.vecentek.back.util;

import com.payneteasy.tlv.*;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ToolUtils {


//    public static String token(String username, String password) {
//        String token = "";
//        try {
//            //过期时间
//            Date date = new Date(System.currentTimeMillis() + Constants.EXPIRE_DATE);
////            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////            System.out.println(sf.format(date));
//            //秘钥及加密算法
//            Algorithm algorithm = Algorithm.HMAC256(Constants.TOKEN_SECRET);
//            //设置头部信息
//            Map<String, Object> header = new HashMap<>();
//            header.put("typ", "JWT");
//            header.put("alg", "HS256");
//            //携带username，password信息，生成签名
//            token = JWT.create()
//                    .withHeader(header)
//                    .withClaim("username", username)
//                    .withClaim("password", password).withExpiresAt(date)
//                    .sign(algorithm);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//        return token;
//    }

//    public static boolean verify(String token) {
//        /**
//         * @desc 验证token，通过返回true
//         * @params [token]需要校验的串
//         **/
//        try {
//            Algorithm algorithm = Algorithm.HMAC256("ZCfasfhuaUUHufguGuwu2020BQWE");
//            JWTVerifier verifier = JWT.require(algorithm).build();
//            DecodedJWT jwt = verifier.verify(token);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    public static int findIntByTlv(BerTlvs tlvs, int tag1, int tag2)  {
        BerTlv berTlv=null;
        if(tag1==0){
            berTlv = tlvs.find(new BerTag(tag2));
        }else {
            berTlv = tlvs.find(new BerTag(tag1,tag2));
        }
        if(berTlv!=null){
            return bytesToIntBig(berTlv.getBytesValue());
        }else{
            return 0;
        }
    }



    public static int findIntValueByTlv(BerTlvs tlvs, int tag1, int tag2)  {
        BerTlv berTlv=null;
        if(tag1==0){
            berTlv = tlvs.find(new BerTag(tag2));
        }else {
            berTlv = tlvs.find(new BerTag(tag1,tag2));
        }
        if(berTlv!=null){
            return berTlv.getIntValue();
        }else{
            return 0;
        }
    }


    public static String findStringValueByTlv(BerTlvs tlvs,int tag1,int tag2)  {
        BerTlv berTlv=null;
        if(tag1==0){
            berTlv = tlvs.find(new BerTag(tag2));
        }else {
            berTlv = tlvs.find(new BerTag(tag1,tag2));
        }
        if(berTlv!=null){
            byte[] bytesValue1 = berTlv.getBytesValue();
            String value = null;
            try {
                value = new String(bytesValue1,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return  value;
        }else{
            return "";
        }
    }

//    public static String findASCIValueByTlv(BerTlvs tlvs,int tag1,int tag2)  {
//        BerTlv berTlv=null;
//        if(tag1==0){
//            berTlv = tlvs.find(new BerTag(tag2));
//        }else {
//            berTlv = tlvs.find(new BerTag(tag1,tag2));
//        }
//        if(berTlv!=null){
//            byte[] bytesValue1 = berTlv.getBytesValue();
//            return HexUtil.toHexString(bytesValue1);
//        }else{
//            return "";
//        }
//    }

    public static byte[] findByteValueByTlv(BerTlvs tlvs,int tag,int tagend){
        BerTlv berTlv=null;
        if(tag==0){
            berTlv = tlvs.find(new BerTag(tagend));
        }else {
            berTlv = tlvs.find(new BerTag(tag,tagend));
        }
        byte[] bytesValue = berTlv.getBytesValue();
        return  bytesValue;
    }


    //返回成功结果
    //返回异常结果

//    public static byte[] returnSuccessResult(){
//        BerTlvBuilder berTlvBuilder = new BerTlvBuilder();
//        //看手机端是否能够接受String,不行就换addintHex
//        berTlvBuilder.addByte(new BerTag(Constants.RESULT_CODE_TAG), Constants.RESULT_SUCCESS_VALUE);
////        berTlvBuilder.addText(new BerTag(Constants.RESULT_CODE_TAG), Constants.RESULT_SUCCESS_VALUE);
//        byte[] bytes = berTlvBuilder.buildArray();
//        return  bytes;
//    }
//    public static byte[] returnErrorResult(String errorMessage){
//        BerTlvBuilder berTlvBuilder = new BerTlvBuilder();
//        berTlvBuilder.addByte(new BerTag(Constants.RESULT_CODE_TAG), Constants.RESULT_ERROR_VALUE);
//        berTlvBuilder.addBytes(new BerTag(Constants.RESULT_MESSAGE_TAG), errorMessage.getBytes());
//        byte[] bytes = berTlvBuilder.buildArray();
//        return  bytes;
//    }

//    //返回成功指定结果结果
//    public static byte[] returnSuccessResult(String errorMessage){
//        BerTlvBuilder berTlvBuilder = new BerTlvBuilder();
//        berTlvBuilder.addByte(new BerTag(Constants.RESULT_CODE_TAG), Constants.RESULT_MESSAGE);
//        berTlvBuilder.addBytes(new BerTag(Constants.RESULT_MESSAGE_TAG), errorMessage.getBytes());
//        byte[] bytes = berTlvBuilder.buildArray();
//        return  bytes;
//    }

    //添加时间
    public static Date goToDayChange(int num , Date oldTime){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(oldTime);
//        calendar.add(Calendar.DATE,num); //加日期
        calendar.add(Calendar.MONTH,num); //加月
//        calendar.add(Calendar.HOUR,num); //加小时
        return calendar.getTime();
    }

    public static String getISO8601Timestamp(Date date){
        TimeZone tz = TimeZone.getTimeZone("GMT+8");
        DateFormat df = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        df.setTimeZone(tz);
        String nowAsISO = df.format(date);
        return nowAsISO;
    }

    public static Date getISO8601Date(String date) throws ParseException {
        TimeZone tz = TimeZone.getTimeZone("GMT+8");
        DateFormat df = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        df.setTimeZone(tz);
        Date parse = df.parse(date);
        return parse;
    }

    //System.arraycopy()方法 bt1与bt2合并为bt3
    public static byte[] byteMerger(byte[] bt1, byte[] bt2){
        byte[] bt3 = new byte[bt1.length+bt2.length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
        return bt3;
    }

    // int 转成四个字节 byte[]
    public static byte[] intToByteArray(int i) {
        byte[] result = new byte[4];
        result[0] = (byte)((i >> 24) & 0xFF);
        result[1] = (byte)((i >> 16) & 0xFF);
        result[2] = (byte)((i >> 8) & 0xFF);
        result[3] = (byte)(i & 0xFF);
        return result;
    }
    // int 转成 一个字节byte[]
    public static byte[] intToByteOneByteArray(int i) {
        byte[] result = new byte[1];
        result[0] = (byte)(i & 0xFF);
        return result;
    }

    // int 转成 一个字节byte[]
    public static byte[] intToByteTwoByteArray(int i) {
        byte[] result = new byte[2];
        result[0] = (byte)((i >> 8) & 0xFF);
        result[1] = (byte)(i & 0xFF);
        return result;
    }

    /**
     * @description: 将byte数组扩容成指定的数组长度，空位填充0
     * @author liujz
     * @date 2022/10/25 12:05
     * @version 1.0
     */
    public static byte[] byteMergerFull0(byte[] bt1,int length){
        if(bt1.length==length){
            return bt1;
        }
        byte[] bt3 = new byte[length];
        int arrLength=length- bt1.length;
        byte[] bt0 = new byte[arrLength];
        System.arraycopy(bt0, 0, bt3, 0, bt0.length);
        System.arraycopy(bt1, 0, bt3, bt0.length, bt1.length);
        return bt3;
    }

    public static byte[] byteMergerFull00(byte[] bt1,int length){
        if(bt1.length==length){
            return bt1;
        }
        byte[] bt3 = new byte[length];
        int arrLength=length- bt1.length;
        System.arraycopy(bt1, 0, bt3, arrLength, bt1.length);
        return bt3;
    }

//    public static byte[] byteMergerFullFF(byte[] bt1,int length){
//        String myString = HexUtil.toHexString(bt1);
//        StringBuffer buffer = new StringBuffer(myString);
//        int byteLength=length*2;
//        while (buffer.length()<byteLength){
//            buffer.append("f");
//        }
//        return HexUtil.parseHex(buffer.toString());
//    }

    /**
     * 字符串转换为Ascii
     * @param value
     * @return
     */
    public static String stringToAscii(String value)
    {
        StringBuffer sbu = new StringBuffer();
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if(i != chars.length - 1)
            {
                sbu.append((int)chars[i]).append(",");
            }
            else {
                sbu.append((int)chars[i]);
            }
        }
        return sbu.toString();
    }

    /**
     * Ascii转换为字符串
     * @param value
     * @return
     */
    public static String asciiToString(String value)
    {
        StringBuffer sbu = new StringBuffer();
        String[] chars = value.split(",");
        for (int i = 0; i < chars.length; i++) {
            sbu.append((char) Integer.parseInt(chars[i]));
        }
        return sbu.toString();
    }

    // Date 转成 long
    public static long getSecondTimestamp(Date date){
        if (null == date) {
            return 0;
        }
        String timestamp = String.valueOf(date.getTime());
        int length = timestamp.length();
        if (length > 3) {
            return Long.valueOf(timestamp.substring(0,length-3));
        } else {
            return 0;
        }
    }

    public static int bytesToIntBig(byte[] src ) {
        int value;
        value = (int) (((src[2] & 0xFF) << 24)
                | ((src[3] & 0xFF) << 16)
                | ((src[4] & 0xFF) << 8)
                | (src[5] & 0xFF));
        return value;
    }

    /**
     * 去除C库传参中的占位符
     * @param string
     * @return
     */
    public static String removePlaceholder(String string) {
        string = string.replaceAll("\\p{C}", "");
        return string;
    }

}

