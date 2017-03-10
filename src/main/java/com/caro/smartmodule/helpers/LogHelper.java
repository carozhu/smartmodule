package com.caro.smartmodule.helpers;

import android.os.Environment;
import android.util.Log;


import com.caro.smartmodule.utils.DeviceInfoUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;


/**
 * 日志管理类

 */
public class LogHelper {
    public static final String ROOT = Environment.getExternalStorageDirectory().getPath() + "/debugfolder/";
    public static final String CAMERA = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/";
    public static final String CACHE_IMG ="/cache/images/";
    /**
     * 应用日志目录文件
     */
    public static String APP_LOG_PATH = ROOT + "log/";


    /**
     * 日志文件路径
     */
    public static String LOGFILE = APP_LOG_PATH + "log.txt";

    /**
     * 日志开关
     */
    private static final boolean LOG_OPEN_DEBUG = true;
    private static final boolean LOG_OPEN_POINT = false;


    /**
     * 日志类型开关，必须 LOG_OPEN_DEBUG = true的时候才能启作用
     */
    private static boolean logOpeni = true;
    private static boolean logOpend = true;
    private static boolean logOpenw = true;
    private static boolean logOpene = true;


    /**
     * 日志目录
     */
    private static final String PATH_LOG_INFO = APP_LOG_PATH + "info/";
    private static final String PATH_LOG_WARNING = APP_LOG_PATH + "warning/";
    public static final String PATH_LOG_ERROR = APP_LOG_PATH + "error/";
    private static final String AUTHOR = "caro";
    public static final boolean ENABLE_DEBUG = false;


    public static void d(String tag, String message) {
        if (message != null && message != null) {
            if (LOG_OPEN_DEBUG && logOpend) {
                Log.d(tag, AUTHOR + message);
            }
            if (LOG_OPEN_POINT)
                point(PATH_LOG_INFO, tag, message);
        }


    }


    public static void i(String tag, String message) {
        if (message != null && message != null) {
            if (LOG_OPEN_DEBUG && logOpeni) {
                Log.i(tag, AUTHOR + message);
            }
            if (LOG_OPEN_POINT)
                point(PATH_LOG_INFO, tag, message);
        }


    }


    public static void w(String tag, String message) {
        if (message != null && message != null) {
            if (LOG_OPEN_DEBUG && logOpenw) {
                Log.w(tag, AUTHOR + message);
            }
            if (LOG_OPEN_POINT)
                point(PATH_LOG_WARNING, tag, message);
        }


    }


    public static void e(String tag, String message) {
        if (message != null && message != null) {
            if (LOG_OPEN_DEBUG && logOpene) {
                Log.e(tag, AUTHOR + message);
            }
            if (LOG_OPEN_POINT)
                point(PATH_LOG_ERROR, tag, message);
        }


    }

    public static void e(String tag, String message, Throwable e) {
        if (message != null && message != null) {
            if (LOG_OPEN_DEBUG && logOpene) {
                Log.e(tag, AUTHOR + message, e);
            }
            if (LOG_OPEN_POINT)
                point(PATH_LOG_ERROR, tag, message);
        }

    }


    public static void point(String path, String tag, String msg) {
        if (DeviceInfoUtil.isSDAva()) {
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("",
                    Locale.SIMPLIFIED_CHINESE);
            dateFormat.applyPattern("yyyy");
            path = path + dateFormat.format(date) + "/";
            dateFormat.applyPattern("MM");
            path += dateFormat.format(date) + "/";
            dateFormat.applyPattern("dd");
            path += dateFormat.format(date) + ".log";
            dateFormat.applyPattern("[yyyy-MM-dd HH:mm:ss]");
            String time = dateFormat.format(date);
            File file = new File(path);
            if (!file.exists())
                createDipPath(path);
            BufferedWriter out = null;
            try {
                out = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(file, true)));
                out.write(time + " " + tag + " " + msg + "\r\n");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 根据文件路径 递归创建文件
     *
     * @param file
     */
    public static void createDipPath(String file) {
        String parentFile = file.substring(0, file.lastIndexOf("/"));
        File file1 = new File(file);
        File parent = new File(parentFile);
        if (!file1.exists()) {
            parent.mkdirs();
            try {
                file1.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * A little trick to reuse a formatter in the same thread
     */
    private static class ReusableFormatter {


        private Formatter formatter;
        private StringBuilder builder;


        public ReusableFormatter() {
            builder = new StringBuilder();
            formatter = new Formatter(builder);
        }


        public String format(String msg, Object... args) {
            formatter.format(msg, args);
            String s = builder.toString();
            builder.setLength(0);
            return s;
        }
    }


    private static final
    ThreadLocal<ReusableFormatter>
            thread_local_formatter =
            new ThreadLocal<ReusableFormatter>() {
                protected ReusableFormatter initialValue() {
                    return new ReusableFormatter();
                }
            };


    public static String format(String msg, Object... args) {
        ReusableFormatter formatter = thread_local_formatter.get();
        return formatter.format(msg, args);
    }
}