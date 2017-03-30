package com.security.util;

import android.util.Log;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 3DES加密解密 --- > 参考来自 http://www.jianshu.com/p/028fe67e6c58
 * key长度为24个字符，不足可补零
 * IV向量必须为8位<br/> （CBC模式才需要的IV向量）
 * <p>
 * ---------Base64提升安全性!!!-----------
 * 使用Base64.encodeToString(byte[], Base64.NO_WRAP);来将加密后的byte[]转为String<br/>
 * 使用Base64.decode(String, Base64.NO_WRAP);将转为String的加密字符转换为byte[]<br/>
 * <p>
 * new String(byte[])将解密后的byte[]转为String<br/>
 */
public class DES3Util {
    private static final String TAG = "DES3Util";
    private static final String DES3 = "DESede";

    ///////////////////////////////////////////////////////////////////////////
    // DES3
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 3DES ECB模式加密
     */
    public static byte[] des3EncodeECB(byte[] key, byte[] data) {
        try {
            SecretKey DESKey = new SecretKeySpec(key, DES3);    //生成密钥
            Cipher cipher = Cipher.getInstance(DES3 + "/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, DESKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * 3DES ECB模式解密
     */
    public static byte[] des3DecodeECB(byte[] key, byte[] data) {
        try {
            SecretKey DESKey = new SecretKeySpec(key, DES3);    //生成密钥
            Cipher cipher = Cipher.getInstance(DES3 + "/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, DESKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * 3DES CBC模式加密
     */
    public static byte[] des3EncodeCBC(byte[] key, byte[] iv, byte[] data) {
        try {
            SecretKey DESKey = new SecretKeySpec(key, DES3);    //生成密钥
            Cipher cipher = Cipher.getInstance(DES3 + "/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, DESKey, ips);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * 3DES CBC模式解密
     */
    public static byte[] des3DecodeCBC(byte[] key, byte[] iv, byte[] data) {
        try {
            SecretKey DESKey = new SecretKeySpec(key, DES3);    //生成密钥
            Cipher cipher = Cipher.getInstance(DES3 + "/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, DESKey, ips);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * 根据字符串生成密钥字节数组
     *
     * @param keyStr 密钥字符串
     */
    private static byte[] build3DesKey(String keyStr) {
        try {
            byte[] key = new byte[24];    //声明一个24位的字节数组，默认里面都是0
            byte[] temp = keyStr.getBytes("UTF-8");    //将字符串转成字节数组
            if (key.length > temp.length) {
                //如果temp不够24位，则拷贝temp数组整个长度的内容到key数组中
                System.arraycopy(temp, 0, key, 0, temp.length);
            } else {
                //如果temp大于24位，则拷贝temp数组24个长度的内容到key数组中
                System.arraycopy(temp, 0, key, 0, key.length);
            }
            return key;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
            return null;
        }
    }
}