package com.vecentek.back.constant;

import java.util.concurrent.TimeUnit;

/**
 * @Date: 2021/9/5 上午12:03
 * @Author ls
 * 授权中心公钥常量
 */
public class JwtConstant {

    public static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkwsrV+i+oN++nJN7maCzDCvaCHw6BS3lEbRgDVNpK75+QVzbXDCCwUm4hIY/9JiK+3o28PEEfQL/ZY81AbLd2UiX1o8Byc/hwrTRrfSc9TIXPpkRnfXvsUo1cqJReJCHviP0Su46hZYRNI3QFVSmVw5iPndT6k+Pup3K97pA5e2ylhdLduoFbHAyqromJN50CmCcXy0EkRE0rCeyXLIpiMePoiTR6m68VLAZf1+q+IJCMXyAsBPxRQjB2lGk2O+0Odt2f6WJeSJa0LM/RDoEGIdmMW3EJo2va/ECS0IxjA3bXe7ZIfwXgKtiofGd3uxMzpS1Ir2DYW4zlbblL2wuLQIDAQAB";

    // JWT 中存储用户信息的key
    public static final String JWT_USER_INFO_KEY = "dkserver-user";
    // JWT 中存储用户信息的key
    public static final String JWT_FACTORY_INFO_KEY = "Authorization";

    // 授权中心服务ID
    public static final String AUTHORITY_CENTER_SERVICE_ID = "dkserver-authority-center";


    public static final String PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCTCytX6L6g376ck3uZoLMMK9oIfDoFLeURtGANU2krvn5BXNtcMILBSbiEhj/0mIr7ejbw8QR9Av9ljzUBst3ZSJfWjwHJz+HCtNGt9Jz1Mhc+mRGd9e+xSjVyolF4kIe+I/RK7jqFlhE0jdAVVKZXDmI+d1PqT4+6ncr3ukDl7bKWF0t26gVscDKquiYk3nQKYJxfLQSRETSsJ7JcsimIx4+iJNHqbrxUsBl/X6r4gkIxfICwE/FFCMHaUaTY77Q523Z/pYl5IlrQsz9EOgQYh2YxbcQmja9r8QJLQjGMDdtd7tkh/BeAq2Kh8Z3e7EzOlLUivYNhbjOVtuUvbC4tAgMBAAECggEAf2+oqtF9lLMU4WI4Npa9VgkTN1NifWafJk7rB+GIPm8jwMYXHri0OYwIrGIlP3iMyxHKV9qqByX37i7Ew6oe9tchMMRTynGtuS84ochj12UIHCpQyFOC3mXwGF9wuEqaOkGy0NXSRkAevStCcZohZ0WCA50rTuCRMiza4QO8hwe/JG2oUGojBRfJyctr+Oy7TQluhMMBRGlZdrgfFLzPj09h0pDfMFrUreb3SWJAt9Rj1n/2+Ah495By5PVnCN2yczoSk3f9tLRPuHlqCuAFBbHNoPl/4dBNbB26KbFEB6/LV8STLN11NFQt1yCSVtwbJ37qRAAUmCqejwVksUZLCQKBgQDQG0OOSF4ukmpK1d2zOHrt5ZZlHIQbQupCes3dcafj0YAmBGbKRPTjEVZy6Bo4oAPe2tAj4RN8rUxS+2pPvoHVohdOFZaCzGKf2zmAkqWUV0OG65BAkwoo9my+vit2zT81CFBtx09lbmE58is9K2PquBBcC5VSlr4wXdNuY8FFGwKBgQC04lhK3GDAwsVy2n1bZzveXmN9X6APH5JqlG4Y1Cl85H2u5GBDXstAyknTOoh+zZ8xZQ+ol7pdLFK1yikAnCuQCYRBcwjT8EjmijwxNhmrfFQJvHsQVtZFmTn+MmpdyNdO1WhImzAUhxeRwKqB8vAjjD6oo3ADwTLs6xYG8R42VwKBgE01wFO6v86xneQUAwQqcVlwM+NhuBXRNs7hdx2wvGSG5u+z9FsZSG4ykYtiV9Aodicpsc2OBUfdmBK1mtTpToxvbgwXcFco83JxyJzOS8aEtdYJ8eQzabKW8vO15LRYJBpN8mE4cFmmoUX5dldkkNaV4eP0nDsfGIMjXH15X0x/AoGAIJtk2yna7yStP0UPr/huHGnbeXBRhChm1nunVSZ5zB/MYEie6cYe0bElmcRFcsZSWi8joXqoh79Dsa2dspR0beSpSLsZ7kq0eYqVyzE7c2RYKeEmNg3RVn6Gq8Ie1oUr+vQRdnUzN6AaqZWXpxFKJL5VVUURHjwnl/nQ4LN6hMUCgYEAjPKO+jXdCR7Nf+Pze42bjReKvg6XdoPbBzmsPX31w0gUEv0uxNiOJk7pXCCtTVFil5TGy1OeK9Ml1cKT+4hNX1ZsKW5CxoCuugYKMCqJut/NiDIdgxJM8XIWhOGjW0pK4n3vRDItNa/RiUmBVpd6KGiAoHTne4Wn8TyEawKVny8=";

    /**
     * 默认 TOKEN 超时时间, 8 小时
     */
    public static final Integer DEFAULT_EXPIRE_HOUR = 8;

    public static final TimeUnit TIME_UNIT = TimeUnit.HOURS;
}
