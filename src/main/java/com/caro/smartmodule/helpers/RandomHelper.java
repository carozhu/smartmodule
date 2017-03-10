package com.caro.smartmodule.helpers;

import java.util.Random;

/**
 * Created by caro on 16/8/4.
 */

public class RandomHelper {

    //生成随机字符时，字符来源
    private static String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789中国山东济南";
    private static int str_len = str.length();

    /**
     * 获取指定位数的随机数(各种类型字符混合)
     * @param length
     * @return
     */
    public static String getRandomStr(int length) {
        if (length <= 0) {
            length = 1;
        }
        Random random = new Random();
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < length; i++) {
            res.append(str.charAt(random.nextInt(str_len)));
        }
        return res.toString();
    }

    /**
     * 获取指定位数的随机数(纯数字)
     * @param length 随机数的位数
     * @return String
     */
    public static String getRandomNum(int length) {
        if (length <= 0) {
            length = 1;
        }
        StringBuilder res = new StringBuilder();
        Random random = new Random();
        int i = 0;
        while (i < length) {
            res.append(random.nextInt(10));
            i++;
        }
        return res.toString();
    }
}
