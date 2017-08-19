package com.caro.smartmodule.utils;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.caro.smartmodule.utils.BytesUtils.bytesToHexString;

/**
 * Description the class 字符串工具类
 *
 * @author zou.sq
 * @version 1.0
 */
public class StringUtil {
    private static final int VALUE_10 = 10;
    private static final int VALUE_13 = 13;
    private static final int VALUE_32 = 32;
    private static final int VALUE_60 = 60;
    private static final int VALUE_62 = 62;
    private static final int VALUE_38 = 38;
    private static final int VALUE_34 = 34;
    private static final int VALUE_165 = 165;
    private static final int VALUE_174 = 174;
    private static final int VALUE_8482 = 8482;
    private static final int VALUE_8364 = 8364;
    private static final int VALUE_169 = 169;
    private static final int OffsetBig = 256;
    private static final int OffsetSmall = 16;

    private static final int MOBIL_F_TAG = 3;
    private static final int MOBIL_L_TAG = 7;
    private static final int WEEK_TAG = 7;
    private static final int MOBIL_P_TAG_PREFIX = 6;
    private static final int MOBIL_N_TAG_PREFIX = 10;

    private static final String ENCRYPT_SALTE = "paidui888";
    private static final String TAG = "StringUtil";

    /**
     * 字符串去空格，回车，换行，制表符
     *
     * @param str 要修改的字符串
     * @return 修改完成的字符串
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 将输入的字符串进行html编码
     *
     * @param input 输入的字符串
     * @return html编码后的结果
     */
    public static String htmEncode(String input) {
        if (null == input || "".equals(input)) {
            return input;
        }

        StringBuffer stringbuffer = new StringBuffer();
        int j = input.length();
        for (int i = 0; i < j; i++) {
            char c = input.charAt(i);
            switch (c) {
                case VALUE_60:
                    stringbuffer.append("&lt;");
                    break;
                case VALUE_62:
                    stringbuffer.append("&gt;");
                    break;
                case VALUE_38:
                    stringbuffer.append("&amp;");
                    break;
                case VALUE_34:
                    stringbuffer.append("&quot;");
                    break;
                case VALUE_169:
                    stringbuffer.append("&copy;");
                    break;
                case VALUE_174:
                    stringbuffer.append("&reg;");
                    break;
                case VALUE_165:
                    stringbuffer.append("&yen;");
                    break;
                case VALUE_8364:
                    stringbuffer.append("&euro;");
                    break;
                case VALUE_8482:
                    stringbuffer.append("&#153;");
                    break;
                case VALUE_13:
                    if (i < j - 1 && input.charAt(i + 1) == VALUE_10) {
                        stringbuffer.append("<br>");
                        i++;
                    }
                    break;
                case VALUE_32:
                    if (i < j - 1 && input.charAt(i + 1) == ' ') {
                        stringbuffer.append(" &nbsp;");
                        i++;
                        break;
                    }
                default:
                    stringbuffer.append(c);
                    break;
            }
        }
        return new String(stringbuffer.toString());
    }

    /**
     * 判断字符串是否为null或者空字符串
     *
     * @param input 输入的字符串
     * @return 如果为null或者空字符串，返回true；否则返回false
     */
    public static boolean isNullOrEmpty(String input) {
        if (null == input || "".equals(input)) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串中是否含有中文字符
     *
     * @param s 需要判断的字符串
     * @return true为包含中文
     */
    public static boolean containChinese(String s) {
        if (null == s) {
            return false;
        }

        Pattern pattern = Pattern.compile(".*[\u4e00-\u9fbb]+.*");
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }

    /**
     * 判断输入的是否为汉字字符
     *
     * @return 是否为中文
     */
    public static boolean isChinese(char a) {
        // return (v >=19968 && v <= 171941);
        return String.valueOf(a).matches("[\u4E00-\u9FA5]"); // 利用正则表达式，经测试可以区分开中文符号
    }

    /**
     * 判断输入的是否为汉字字符
     *
     * @return 是否为中文
     */
    public static boolean isChinese(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!isChinese(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取MD5加密后Hash字符串
     *
     * @param strOriginal 初始字符串
     * @return MD5加密后Hash字符串
     */
    private static String getMd5Hash(String strOriginal) {
        StringBuilder sbList = new StringBuilder();
        try {
            MessageDigest mMD5 = MessageDigest.getInstance("MD5");
            byte[] data = strOriginal.getBytes("utf-8");
            byte[] dataPWD = mMD5.digest(data);
            for (int offset = 0; offset < dataPWD.length; offset++) {
                int i = dataPWD[offset];
                if (i < 0) {
                    i += OffsetBig;
                }
                if (i < OffsetSmall) {
                    sbList.append("0");
                }
                sbList.append(Integer.toHexString(i));
            }
            return sbList.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.w(TAG, e.toString());
        } catch (UnsupportedEncodingException e) {
            Log.w(TAG, e.toString());
        }
        return null;
    }

    /**
     * 获取MD5加密后Hash字符串
     *
     * @param strOriginal 初始字符串
     * @param strSalt     种子字符串
     * @return MD5加密后Hash字符串
     */
    public static String getMd5Hash(String strOriginal, String strSalt) {
        String mStrSalt;
        String mStrOriginal = strOriginal;
        // 如果调用未给Salt值,则默认
        if (strSalt == null) {
            mStrSalt = ENCRYPT_SALTE;
        } else {
            mStrSalt = strSalt;
        }
        mStrOriginal = mStrOriginal + mStrSalt;
        return getMd5Hash(mStrOriginal);
    }

    /**
     * @param userMobile 需要处理的手机号码
     * @return String 处理后的手机号码
     * @throws
     * @Method: getProcessedDrawMobile
     * @Description: 处理手机号码
     */
    public static String getProcessedMobile(String userMobile) {
        String mProcessedDrawMobile = "";
        if (!StringUtil.isNullOrEmpty(userMobile)) {
            Log.d(TAG, userMobile);
            // 判断是否是+86开头的手机号码
            if ('+' == userMobile.charAt(0) && userMobile.length() >= MOBIL_N_TAG_PREFIX) {
                mProcessedDrawMobile = userMobile.substring(0, MOBIL_P_TAG_PREFIX) + "****"
                        + userMobile.substring(MOBIL_N_TAG_PREFIX);
            } else if ('+' != userMobile.charAt(0) && userMobile.length() >= MOBIL_L_TAG) {
                mProcessedDrawMobile = userMobile.substring(0, MOBIL_F_TAG) + "****"
                        + userMobile.substring(MOBIL_L_TAG);
            } else {
                mProcessedDrawMobile = userMobile;
            }
        }
        return mProcessedDrawMobile;
    }

    /**
     * 获取当期是星期几（从星期天开始）
     *
     * @param weeknum 当前是第几天（0-6）
     * @return 星期*
     */
    public static String getDayOfWeek(int weeknum) {
        weeknum--;
        if (weeknum > WEEK_TAG) {
            weeknum = weeknum % WEEK_TAG;
        }

        if (weeknum < 0) {
            weeknum = -weeknum;
        }

        String[] weekArray = new String[]{"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        return weekArray[weeknum];

    }

    /**
     * 重载方法，获取当期是星期几
     *
     * @param c 日历
     * @return 星期*
     */
    public static String getDayOfWeek(Calendar c) {
        return getDayOfWeek(c.get(Calendar.DAY_OF_WEEK));
    }

    /**
     * 检查是不是中文
     *
     * @param str 检查字符串
     * @return boolean 是否为中文
     * @throws
     * @Method: checkStringIsChinses
     */
    public static boolean checkStringIsChinese(String str) {
        if (null == str) {
            return false;
        }
        Pattern pattern = Pattern.compile("[\\u4E00-\\u9FA5]+");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 转换保留小数位 字符串
     *
     * @param i      小数位数
     * @param numStr 数字字符串
     * @return String
     */
    public static String getDecimalFormat(int i, String numStr) {
        try {
            if (numStr != null && !"".equals(numStr)) {
                BigDecimal bd = new BigDecimal(numStr);
                bd = bd.setScale(i, BigDecimal.ROUND_HALF_UP);

                return bd.toString();
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 转换保留小数位
     *
     * @param i   小数位数
     * @param num 要转换的数字
     * @return double 返回类型
     */
    public static double decimalFormat(int i, Double num) {
        String temp = getDecimalFormat(i, num.toString());
        double numFormat = Double.valueOf(temp);
        return numFormat;
    }

    /**
     * 检查是否是字符串
     *
     * @param input 被检查字符串
     * @return 是否是数字组成的字符串，包括0开头;<br>
     * 如果为空或者空字符串，返回true；比如："011"返回true，"a11"返回false
     */
    public static boolean isNumberString(String input) {
        if (!isNullOrEmpty(input)) {
            if (input.matches("[0-9]+")) {
                return true;
            }
        }

        return false;
    }

    /**
     * 检查输入字符串是否是合法的http地址，这个检查方法不完整，只是初步的检查是否以http://开头兵器后面有字符串
     *
     * @param input 输入的网址
     * @return 输入字符串是否合法
     */
    public static boolean isHttpUrl(String input) {
        return false;
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     *
     * @param str
     * @return String
     */
    public static String filterZeroAndDot(String str) {
        if (!isNullOrEmpty(str) && str.indexOf(".") > 0) {
            // 去掉多余的0
            str = str.replaceAll("0+?$", "");
            // 如最后一位是.则去掉
            str = str.replaceAll("[.]$", "");
        }
        return str;
    }

    public static String getDecimalFormatString(double input, int digits) {
        BigDecimal bd = new BigDecimal(input);
        bd = bd.setScale(digits, BigDecimal.ROUND_HALF_UP);

        return filterZeroAndDot(bd.toString());
    }

    /**
     * 半角转换为全角（用于textview换行不整齐时使用）
     *
     * @param input
     * @return
     */
    public static String toDBC(String input) {
        if (input == null) {
            return "";
        }
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    public static String sortString(String str) {
        String[] orderDishID = str.split(",");
        Arrays.sort(orderDishID);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < orderDishID.length; i++) {
            sb.append(orderDishID[i]);
            if (i + 1 < orderDishID.length) {
                sb.append(",");
            }
        }
        orderDishID = null;
        str = null;
        return sb.toString();
    }

    /**
     * 从第一个分隔符到结尾，去掉分隔符前面的内容(去前缀)
     *
     * @param resStr     内容字符串
     * @param spreateStr 前缀标记
     * @return
     */
    public static String subStringFromSpreateToEnd(String resStr, String spreateStr) {
        if (!StringUtil.isNullOrEmpty(resStr) && !StringUtil.isNullOrEmpty(spreateStr) && resStr.contains(spreateStr)) {
            resStr = resStr.substring(resStr.indexOf(spreateStr) + 1);
        }
        return resStr;
    }

    /**
     * 为字符串加入分隔符，如果为空
     *
     * @param resStr
     * @param appendStr
     * @param spreateStr
     * @return
     */
    public static String appendStringWithSpreate(String resStr, String appendStr, String spreateStr) {
        if (!StringUtil.isNullOrEmpty(spreateStr)) {
            if (StringUtil.isNullOrEmpty(resStr)) {
                resStr = appendStr;
            } else {
                resStr += spreateStr + appendStr;
            }
        }
        return resStr;
    }

    public static boolean isTelNumAvailable(String telNum) {
        boolean result = false;
        if (!isNullOrEmpty(telNum) && telNum.matches("^1[3458]{1}[0-9]{9}$")) {
            result = true;
        }
        return result;
    }

    /**
     * 是否是汉字，英文，数字或者其中几个组成
     *
     * @param s
     * @return boolean
     */
    public static boolean isValidEngOrChOrNum(String s) {
        if (null == s) {
            return false;
        }
        Pattern pattern = Pattern.compile("[\u4E00-\u9FA5A-Za-z0-9]*$");
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }

    /**
     * @param
     * @return String
     * @Title: listToString
     * @Description: 把list集合数据转化成带逗号的字符串
     */
    public static String listToString(List<String> list) {
        StringBuilder sb = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i < list.size() - 1) {
                    sb.append(list.get(i) + ",");
                } else {
                    sb.append(list.get(i));
                }
            }
        }
        return sb.toString();
    }

    /**
     * 将指定byte数组转换成16进制字符串
     *
     * @param b
     * @return
     */
    public static String byteToHexString(byte[] b) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            hexString.append(hex.toUpperCase());
        }
        return hexString.toString();
    }



    public static String getWebCon(String domain) {
        // System.out.println("开始读取内容...("+domain+")");
        StringBuffer sb = new StringBuffer();
        try {
            java.net.URL url = new java.net.URL(domain);
            BufferedReader in = new BufferedReader(new InputStreamReader(url
                    .openStream()));
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            in.close();
        } catch (Exception e) { // Report any errors that arise
            sb.append(e.toString());
            System.err.println(e);
            System.err
                    .println("Usage:   java   HttpClient   <URL>   [<filename>]");
        }
        return sb.toString();
    }

    /**
     * 字符串转stream
     *
     * @param str
     * @return
     */
    public static InputStream StringToInputStream(String str) {
        if (TextUtils.isEmpty(str))
            return null;
        ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());
        return stream;
    }

    /**
     * 输入流转字符串
     *
     * @param is
     * @return
     */
    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();


        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return sb.toString();
    }

    /**
     * 字符串替换
     *
     * @param line
     * @param oldString
     * @param newString
     * @return
     */
    public static final String replace(String line, String oldString,
                                       String newString) {
        if (line == null) {
            return null;
        }
        int i = 0;
        if ((i = line.indexOf(oldString, i)) >= 0) {
            char[] line2 = line.toCharArray();
            char[] newString2 = newString.toCharArray();
            int oLength = oldString.length();
            StringBuffer buf = new StringBuffer(line2.length);
            buf.append(line2, 0, i).append(newString2);
            i += oLength;
            int j = i;
            while ((i = line.indexOf(oldString, i)) > 0) {
                buf.append(line2, j, i - j).append(newString2);
                i += oLength;
                j = i;
            }
            buf.append(line2, j, line2.length - j);
            return buf.toString();
        }
        return line;
    }


    /**
     * 检查是否符合手机号码格式
     * 用正则表达式匹配输入的手机号码是否正确
     *
     * @param phoneNum
     * @return
     */
    public static boolean isMobileNO(String phoneNum) {
        final String regx = "^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";


        Pattern p = Pattern.compile(regx);
        Matcher m = p.matcher(phoneNum);


        return m.matches();
    }


    /**
     * 加密
     *
     * @param password
     * @return
     */
    public static String encryptionKey(String key1 ,String key2 ,String password) {
        byte[] keyByte1 = key1.getBytes();
        byte[] keyByte2 = key2.getBytes();
        byte[] pwdByte = password.getBytes();
        for (int i = 0; i < pwdByte.length; i++) {
            pwdByte[i] = (byte) (pwdByte[i] ^ keyByte1[i % keyByte1.length]);
        }
        byte[] countByte = new byte[pwdByte.length + keyByte1.length];
        for (int i = 0; i < countByte.length; i++) {
            if (i < pwdByte.length)
                countByte[i] = pwdByte[i];
            else
                countByte[i] = keyByte1[i - pwdByte.length];
        }
        for (int i = 0; i < countByte.length; i++) {
            countByte[i] = (byte) (countByte[i] ^ keyByte2[i % keyByte2.length]);
        }
        return bytesToHexString(countByte);
    }

    /**
     * 解密
     *
     * @param password
     * @return
     */
    public static String decryptionKey(String key1 ,String key2 ,String password) {
        byte[] keyByte1 = key1.getBytes();
        byte[] keyByte2 = key2.getBytes();
        byte[] pwdByte = hexStr2Bytes(password);

        for (int i = 0; i < pwdByte.length; i++) {
            pwdByte[i] = (byte) (pwdByte[i] ^ keyByte2[i % keyByte2.length]);
        }

        byte[] lastByte = new byte[pwdByte.length - keyByte1.length];
        for (int i = 0; i < lastByte.length; i++) {
            lastByte[i] = pwdByte[i];
        }
        for (int i = 0; i < lastByte.length; i++) {
            lastByte[i] = (byte) (lastByte[i] ^ keyByte1[i % keyByte1.length]);
        }

        return new String(lastByte);
    }


    /**
     * 十六进制转换字符串
     *
     * @param hexStr str Byte字符串(Byte之间无分隔符 如:[616C6B])
     * @return String 对应的字符串
     */
    public static String hexStr2Str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;

        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }

    /**
     * bytes字符串转换为Byte值
     *
     * @param src src Byte字符串，每个Byte之间没有分隔符
     * @return byte[]
     */
    public static byte[] hexStr2Bytes(String src) {
        int m = 0, n = 0;
        int l = src.length() / 2;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            m = i * 2 + 1;
            n = m + 1;
            ret[i] = Byte.decode("0x" + src.substring(i * 2, m) + src.substring(m, n));
        }
        return ret;
    }

    /**
     * hightlight text
     *
     * @param start
     * @param end
     * @param text
     */
    public static void highlight(int start, int end, TextView text,int color) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(text.getText().toString());
        ForegroundColorSpan span = new ForegroundColorSpan(color);
        spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setText(spannable);
    }

    public static String getHightLightText(String text) {
        Pattern p = Pattern.compile("<em>([^</em>]*)");//匹配<title>开头，</title>结尾的文档
        Matcher m = p.matcher(text);//开始编译
        String keyword = null;
        while (m.find()) {
            //LogManager.d("hight light:", m.group(1));
            keyword = m.group(1);
        }
        return keyword;
    }

    //判断email格式是否正确
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    /**
     * 关键字高亮显示
     *
     * @param target 需要高亮的关键字
     */
    public static  String highlight(String strold, String target) {
        String temp = strold.toString();
        SpannableStringBuilder spannable = new SpannableStringBuilder(temp);
        CharacterStyle span = null;

        Pattern p = Pattern.compile(target);
        Matcher m = p.matcher(temp);
        while (m.find()) {
            span = new ForegroundColorSpan(Color.RED);// 需要重复！ //span = new
            // ImageSpan(drawable,ImageSpan.XX);//设置现在图片
            spannable.setSpan(span, m.start(), m.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannable.toString();
    }

    // 清除掉所有特殊字符
    public  static  String clearSpecialCharacters(String orgString){
        try {
            String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]_-";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(orgString);
            orgString = m.replaceAll("").replaceAll("\\D+","").trim();
            return orgString;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "what happend!";
    }

    public static List<String> stringsToList(final String[] src) {
        if (src == null || src.length == 0) {
            return null;
        }
        final List<String> result = new ArrayList<String>();
        for (int i = 0; i < src.length; i++) {
            result.add(src[i]);
        }
        return result;
    }

    public static void printHexString(byte[] b){
        for(int i=0;i < b.length;i++){
            String hex=Integer.toHexString(b[i] & 0xFF);
            if(hex.length()==1){
                hex='0' +hex;
            }
            System.out.print(hex.toUpperCase() +" ");
        }
        System.out.print("\n");
    }

    public static void printShortString(short[] b){
        for(int i=0;i < b.length;i++){
            String hex=Integer.toHexString(b[i]);
            if(hex.length()==1){
                hex='0' +hex;
            }
            System.out.print(hex.toUpperCase() +" ");
        }
        System.out.print("\n");
    }

    /**
     * 将字符串转分段成数组
     * @param sourceString
     * @param squrelen
     * @return
     */
    private List<String> splitSqureString(String sourceString, int squrelen) {
        int lengs = sourceString.length() / squrelen;
        int lesslens = sourceString.length() % squrelen;
        Log.i("splitSqureString", "lengs == " + lengs + "  sslens == " + lesslens);
        List<String> stringList = new ArrayList<String>();
        for (int l = 0; l < lengs; l++) {
            String subtemp = sourceString.substring(l * squrelen, l * squrelen + squrelen);
            stringList.add(subtemp);
        }
        if (lesslens > 0) {
            String subtemp = sourceString.substring(lengs * squrelen, lengs * squrelen + lesslens);
            stringList.add(subtemp);
        }

        return stringList;
    }


}
