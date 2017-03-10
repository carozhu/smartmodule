package com.caro.smartmodule.helpers;

import android.content.Context;
import android.widget.Toast;

import com.caro.smartmodule.utils.NetworkUtil;
import com.google.zxing.ReaderException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeoutException;

/**
 * 来自 http://blog.csdn.net/catoop/article/details/50076879
 * 读取网络时间
 * http://www.time.ac.cn -- 此网站比较准确
 *
 * @author SHANHY(365384722@QQ.COM)
 * @date 2015年11月27日
 */
public class GetNetworkDatetimeHelper {

    public static void main(String[] args) {
        String webMyUrl = "http://www.time.ac.cn/";//http://www.time.ac.cn/
        String webUrl1 = "http://www.bjtime.cn";//bjTime
        String webUrl2 = "http://www.baidu.com";//百度
        String webUrl3 = "http://www.taobao.com";//淘宝
        String webUrl4 = "http://www.ntsc.ac.cn";//中国科学院国家授时中心
        String webUrl5 = "http://www.360.cn";//360
        String webUrl6 = "http://www.beijing-time.org";//beijing-time

        System.out.println(getWebsiteDatetime(webMyUrl) + " [webMyUrl]");
        System.out.println(getWebsiteDatetime(webUrl1) + " [bjtime]");
        System.out.println(getWebsiteDatetime(webUrl2) + " [百度]");
        System.out.println(getWebsiteDatetime(webUrl3) + " [淘宝]");
        System.out.println(getWebsiteDatetime(webUrl4) + " [中国科学院国家授时中心]");
        System.out.println(getWebsiteDatetime(webUrl5) + " [360安全卫士]");
        System.out.println(getWebsiteDatetime(webUrl6) + " [beijing-time]");
    }

    /**
     * 获取指定网站的日期时间
     *
     * @param webUrl
     * @return
     * @author SHANHY
     * @date 2015年11月27日
     */
    public static String getWebsiteDatetime(String webUrl) {
        try {
            URL url = new URL(webUrl);// 取得资源对象
            URLConnection uc = url.openConnection();// 生成连接对象
            uc.connect();// 发出连接
            long ld = uc.getDate();// 读取网站日期时间
            Date date = new Date(ld);// 转换为标准时间对象
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);// 输出北京时间
            return sdf.format(date);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定网站的时间戳.
     * 并且在2秒内如果连接不到时间网站服务。则返回本地时间。
     *
     * @param webUrl
     * @return
     * @author caro
     * @date 2016年07月28日
     */
    public static long getNetWorkTimeMills(String webUrl, Context context) {
        return System.currentTimeMillis();
        // TODO: 2016/11/25 网络访问超时优化
//        boolean pingsuccss = NetworkUtil.pingserver(webUrl, 1200);
//        if (!pingsuccss) {
//            Toast.makeText(context, "ping timeout,SocketTimeoutException", Toast.LENGTH_LONG).show();
//            return System.currentTimeMillis();
//
//        }
//        try {
//            URL url = new URL(webUrl);// 取得资源对象
//            URLConnection uc = url.openConnection();// 生成连接对象
//            uc.setConnectTimeout(1500);//超时为2秒
//            uc.setReadTimeout(1500);
//            uc.connect();// 发出连接
//            long curTimeMills = uc.getDate();// 读取网站日期时间
//            return curTimeMills;
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//
//        } catch (SocketTimeoutException e) {
//            e.printStackTrace();
//            Toast.makeText(context, "SocketTimeoutException", Toast.LENGTH_LONG).show();
//            return System.currentTimeMillis();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            Toast.makeText(context, "IOException", Toast.LENGTH_LONG).show();
//            return System.currentTimeMillis();
//        }
//        return System.currentTimeMillis();
    }
}
