package com.caro.smartmodule.utils;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.util.List;

/**
 * network tools
 *
 * @author caro
 */
public class NetworkUtil {


    /**
     * 获取网路连接类型
     *
     * @param context 上下文
     * @return 网络类型
     * 需要添加权限<uses-permission android:name="android.permission.INTERNET"/>
     * 需要添加权限<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
     */
    public String getNetType(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        String result = null;
        if (info != null && info.isAvailable()) {
            if (info.isConnected()) {
                int type = info.getType();
                String typeName = info.getTypeName();
                switch (type) {
                    case ConnectivityManager.TYPE_BLUETOOTH:
                        result = "蓝牙连接   :  " + typeName;
                        break;
                    case ConnectivityManager.TYPE_DUMMY:
                        result = "虚拟数据连接    :  " + typeName;
                        break;
                    case ConnectivityManager.TYPE_ETHERNET:
                        result = "以太网数据连接    :  " + typeName;
                        break;
                    case ConnectivityManager.TYPE_MOBILE:
                        result = "移动数据连接   : " + typeName;
                        break;
                    case ConnectivityManager.TYPE_MOBILE_DUN:
                        result = "网络桥接 :  " + typeName;
                        break;
                    case ConnectivityManager.TYPE_MOBILE_HIPRI:
                        result = "高优先级的移动数据连接 :  " + typeName;
                        break;
                    case ConnectivityManager.TYPE_MOBILE_MMS:
                        result = "运营商的多媒体消息服务  : " + typeName;
                        break;
                    case ConnectivityManager.TYPE_MOBILE_SUPL:
                        result = "平面定位特定移动数据连接  :  " + typeName;
                        break;
                    case ConnectivityManager.TYPE_WIFI:
                        result = "Wifi数据连接   : " + typeName;
                        break;
                    case ConnectivityManager.TYPE_WIMAX:
                        result = "全球微波互联   : " + typeName;
                        break;
                    default:
                        break;
                }
            }
        }
        return result;
    }

    /**
     * 网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Gps是否打开
     *
     * @param context
     * @return
     */
    public static boolean isGpsEnabled(Context context) {
        LocationManager locationManager = ((LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE));
        List<String> accessibleProviders = locationManager.getProviders(true);
        return accessibleProviders != null && accessibleProviders.size() > 0;
    }



    /**
     * 判断当前网络是否是wifi网络并且已连接
     *
     * @param context
     * @return boolean
     */
    public static boolean isWifiEnabled(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI && connectivityManager.getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) {
            return true;
        }
        return false;
    }


    /**
     * 网络检测
     *
     * @param context 上下文
     * @return false:无网络,true:有网络
     */
    public boolean isOnline(Context context) {
        boolean isOnline = false;
        final ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null) {
            isOnline = networkInfo.isAvailable();
        }
        // String netType = "当前网络类型为：" + networkInfo.getTypeName();
        return isOnline;
    }


    /**
     * 获取Wifi下的Ip地址
     * 需要添加权限: <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
     *
     * @param context 上下文
     * @return IP地址
     */
    public String getWifiLocalIpAddress(Context context) {
        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        int ipAddress = info.getIpAddress();
        return intToIp(ipAddress);
    }

    private String intToIp(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
                + "." + ((i >> 24) & 0xFF);

    }

    /**
     * 获取MAC地址
     *
     * @param context       上下文
     * @param replaceSymbol 替换字符,默认替换字符为""
     * @return 返回MAC地址     错误返回12个0
     */
    public String getMacAddress(Context context, String replaceSymbol) {
        String macAddress = "000000000000";
        if (replaceSymbol == null) {
            replaceSymbol = "";
        }
        try {
            WifiManager wifiMgr = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = (null == wifiMgr ? null : wifiMgr
                    .getConnectionInfo());
            if (null != info) {
                if (!TextUtils.isEmpty(info.getMacAddress()))
                    macAddress = info.getMacAddress().replace(":", replaceSymbol);
                else
                    return macAddress;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return macAddress;
        }
        return macAddress;
    }

    /**
     * 正确的打开网络设置
     */
    public static void openSysSetting(Context context) {
        if (android.os.Build.VERSION.SDK_INT > 13) {
            context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
        } else {
            context.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
        }
    }

    /**
     * Get Phone IMEI Number
     *
     * @return imei
     */

    public String getImei(Context c) {
        TelephonyManager telephonyManager = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        return imei;
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * @param server  ip
     * @param timeout
     * @return true
     */
    public static boolean pingserver(String server, int timeout) {
        BufferedReader in = null;
        Runtime r = Runtime.getRuntime();
        String pingcommand = "ping " + server + " -n 1 -w " + timeout;
        try {
            Process p = r.exec(pingcommand);
            if (p == null) {
                return false;
            }
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                if (line.startsWith("reply from")) {
                    return true;
                }
            }

        } catch (SocketTimeoutException socket) {
            socket.printStackTrace();
            return false;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
