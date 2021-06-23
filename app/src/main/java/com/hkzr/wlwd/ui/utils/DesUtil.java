package com.hkzr.wlwd.ui.utils;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DesUtil {
    private final static String DES = "DES";
    private final static String KEY = "11001100";

    public static void main(String[] args) throws Exception {
        String data = "123 456";
        String key = "11001100";
        System.err.println(desCrypto(data.getBytes(), key));
        System.err.println(decrypt(desCrypto(data.getBytes(), key), key));

    }

    /**
     * 加密
     *
     * @param data 要加密数据
     * @return
     */
    public static String desCrypto(String data) {
        String result = null;
        byte[] encryptedData = desCrypto(data.getBytes(), KEY);
        result = toHexString(encryptedData).toUpperCase();
        return result;
    }


    /**
     * Description 根据键值进行加密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    public static byte[] desCrypto(byte[] datasource, String password) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(password.getBytes("UTF-8"));
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            // Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS7Padding");
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            // 现在，获取数据并加密
            // 正式执行加密操作
            return cipher.doFinal(datasource);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加密
     *
     * @param data 要加密数据
     * @return
     */
    public static String desCrypto(String data, String key) {
        String result = null;
        byte[] encryptedData = desCrypto(data.getBytes(), key);
        result = toHexString(encryptedData).toUpperCase();
        return result;
    }

    /**
     * 解密
     *
     * @param src 源
     * @return
     */
    public static String decrypt(String src) {
        String result = null;
        try {
            byte[] byteMi = convertHexString(src);
            result = new String(decrypt(byteMi, KEY), "UTF-8");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Description 根据键值进行解密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] src, String password) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom random = new SecureRandom();
        // 创建一个DESKeySpec对象
        DESKeySpec desKey = new DESKeySpec(password.getBytes());
        // 创建一个密匙工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        // 将DESKeySpec对象转换成SecretKey对象
        SecretKey securekey = keyFactory.generateSecret(desKey);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS7Padding");
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, random);
        // 真正开始解密操作
        return cipher.doFinal(src);
    }

    public static String toHexString(byte b[]) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String plainText = Integer.toHexString(0xff & b[i]);
            if (plainText.length() < 2)
                plainText = "0" + plainText;
            hexString.append(plainText);
        }
        return hexString.toString();
    }

    public static byte[] convertHexString(String ss) {
        byte digest[] = new byte[ss.length() / 2];
        for (int i = 0; i < digest.length; i++) {
            String byteString = ss.substring(2 * i, 2 * i + 2);
            if (byteString.length() == 2) {
                int byteValue = Integer.parseInt(byteString, 16);
                digest[i] = (byte) byteValue;
            }
        }
        return digest;
    }

    // private static byte[] iv = {1,2,3,4,5,6,7,8};
    // public static String encryptDES(String encryptString, String encryptKey)
    // throws Exception {
    // // IvParameterSpec zeroIv = new IvParameterSpec(new byte[8]);
    // IvParameterSpec zeroIv = new IvParameterSpec(iv);
    // SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");
    // Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
    // cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
    // byte[] encryptedData = cipher.doFinal(encryptString.getBytes());
    //
    // return Base64.encode(encryptedData);
    // }
    // public static String decryptDES(String decryptString, String decryptKey)
    // throws Exception {
    // byte[] byteMi = new Base64().decode(decryptString);
    // IvParameterSpec zeroIv = new IvParameterSpec(iv);
    // // IvParameterSpec zeroIv = new IvParameterSpec(new byte[8]);
    // SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");
    // Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
    // cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
    // byte decryptedData[] = cipher.doFinal(byteMi);
    //
    // return new String(decryptedData);
    // }

}