package com.kiegame.mobile.utils;

import java.nio.charset.StandardCharsets;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by: var.
 * Created date: 2020/4/2.
 * Description: AES加解密
 */
public class AES {

    private static final String KEY = "kiegame_ase_test";

    /**
     * AES 加密操作
     *
     * @param data 待加密内容
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
            cipher.init(Cipher.ENCRYPT_MODE, AES.key(), new IvParameterSpec(new byte[cipher.getBlockSize()]));
            byte[] result = cipher.doFinal(bytes);
            return BASE64.encrypt(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES 解密操作
     *
     * @param data 待解密内容
     * @return 返回解密后的数据
     */
    public static String decrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, AES.key(), new IvParameterSpec(new byte[cipher.getBlockSize()]));
            byte[] result = cipher.doFinal(BASE64.decrypt(data));
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成加密秘钥
     *
     * @return 返回生成的密钥
     */
    private static Key key() {
        SecretKey secret = new SecretKeySpec(AES.KEY.getBytes(), "AES");
        return new SecretKeySpec(secret.getEncoded(), "AES");
    }
}
