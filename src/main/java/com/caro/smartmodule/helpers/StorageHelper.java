package com.caro.smartmodule.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class StorageHelper {

    private static final String TAG = StorageHelper.class.getName();
    /**
     * SD卡是否存在
     * @return
     */
    public boolean isSDexist(){
        //SD卡是否存在
        boolean isExist = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
        return isExist;
    }
    
    /**
     * 获取最大内存
     *
     * @return
     */
    public static long getMaxMemory() {

     return Runtime.getRuntime().maxMemory() / 1024;
    }
    
    /** 
     * 判断SDCard是否可用 
     *  
     * @return 
     */  
    public static boolean isSDCardEnable()  
    {  
        return Environment.getExternalStorageState().equals(  
                Environment.MEDIA_MOUNTED);  
  
    }  
  
    /** 
     * 获取SD卡路径 
     *  
     * @return 
     */  
    public static String getSDCardPath()  
    {  
        return Environment.getExternalStorageDirectory().getAbsolutePath()  
                + File.separator;  
    }  
    
    /** 
     * 获取系统存储路径 
     *  
     * @return 
     */  
    public static String getRootDirectoryPath()  
    {  
        return Environment.getRootDirectory().getAbsolutePath();  
    }

    /**
     * 获取当前程序路径
     * @param context
     * @return
     */
    public static String getApplicationFileDir(Context context){
        String dir = context.getFilesDir().getAbsolutePath();
        return dir;
    }

    /**
     * 获取当前程序安装包路径
     * @param context
     * @return
     */
    public static String getApplicationInstallFileDir(Context context){
        String dir = context.getPackageResourcePath();
        return dir;
    }


    /**
     * 获取当前程序默认数据库路径
     * @param context
     * @return
     */
    public static String getApplicationDeafaultDatabaseDir(Context context,String databasename){
        String dir = context.getDatabasePath(databasename).getAbsolutePath();
        return dir;
    }


    /**
     * 检查是否存在SD卡
     *
     * @return
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 创建目录
     *
     * @param context
     * @param dirName
     *            文件夹名称
     * @return
     */
    public static File createFileDir(Context context, String dirName) {
        String filePath;
        // 如SD卡已存在，则存储；反之存在data目录下
        if (hasSdcard()) {
            // SD卡路径
            filePath = Environment.getExternalStorageDirectory()
                    + File.separator + dirName;
        } else {
            filePath = context.getCacheDir().getPath() + File.separator
                    + dirName;
        }
        File destDir = new File(filePath);
        if (!destDir.exists()) {
            boolean isCreate = destDir.mkdirs();
            Log.i(TAG, filePath + " has created. " + isCreate);
        }
        return destDir;
    }

    /**
     * 删除文件（若为目录，则递归删除子目录和文件）
     *
     * @param file
     * @param delThisPath
     *            true代表删除参数指定file，false代表保留参数指定file
     */
    public static void delFile(File file, boolean delThisPath) {
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            File[] subFiles = file.listFiles();
            if (subFiles != null) {
                int num = subFiles.length;
                // 删除子目录和文件
                for (int i = 0; i < num; i++) {
                    delFile(subFiles[i], true);
                }
            }
        }
        if (delThisPath) {
            file.delete();
        }
    }

    /**
     * 获取文件大小，单位为byte（若为目录，则包括所有子目录和文件）
     *
     * @param file
     * @return
     */
    public static long getFileSize(File file) {
        long size = 0;
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] subFiles = file.listFiles();
                if (subFiles != null) {
                    int num = subFiles.length;
                    for (int i = 0; i < num; i++) {
                        size += getFileSize(subFiles[i]);
                    }
                }
            } else {
                size += file.length();
            }
        }
        return size;
    }

    /**
     * 保存Bitmap到指定目录
     *
     * @param dir
     *            目录
     * @param fileName
     *            文件名
     * @param bitmap
     * @throws IOException
     */
    public static void savaBitmap(File dir, String fileName, Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        File file = new File(dir, fileName);
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断某目录下文件是否存在
     *
     * @param dir
     *            目录
     * @param fileName
     *            文件名
     * @return
     */
    public static boolean isFileExists(File dir, String fileName) {
        return new File(dir, fileName).exists();
    }
}
