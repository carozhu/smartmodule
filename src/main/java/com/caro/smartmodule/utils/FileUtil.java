package com.caro.smartmodule.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

/**
 *
 * Description the class 文件帮助类
 * @version 1.0
 * @author zou.sq
 *
 */
public class FileUtil {
    public static final int BUFSIZE = 256;
    public static final int COUNT = 320;
    private static final String TAG = "FileUtils";
    private static final long SIZE_KB = 1024;
    private static final long SIZE_MB = 1048576;
    private static final long SIZE_GB = 1073741824;

    /**
     * 判断指定的文件是否存在
     *
     * @param filePath
     *            文件路径
     * @return 是否存在
     */
    public static boolean isFileExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * 准备文件夹，文件夹若不存在，则创建
     *
     * @param filePath
     *            文件路径
     */
    public static void prepareFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 删除指定的文件或目录
     *
     * @param filePath
     *            文件路径
     */
    public static void delete(String filePath) {
        if (filePath == null) {
            return;
        }
        try {
            File file = new File(filePath);
            delete(file);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    /**
     * 删除指定的文件或目录
     *
     * @param file
     *            文件
     */
    public static void delete(File file) {
        if (file == null || !file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            deleteDirRecursive(file);
        } else {
            file.delete();
        }
    }

    /**
     * 递归删除目录
     *
     * @param dir
     *            文件路径
     */
    public static void deleteDirRecursive(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory()) {
            return;
        }
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        for (File f : files) {
            if (f.isFile()) {
                f.delete();
            } else {
                deleteDirRecursive(f);
            }
        }
        dir.delete();
    }

    /**
     * 取得文件大小
     *
     * @param f
     *            文件
     * @return long 大小
     *
     */
    public long getFileSizes(File f) {
        long s = 0;
        try {
            if (f.exists()) {
                FileInputStream fis = new FileInputStream(f);
                s = fis.available();
            } else {
                f.createNewFile();
            }
        } catch (Exception e) {
            Log.w(TAG, e.toString());
        }
        return s;
    }

    /**
     * 递归取得文件夹大小
     *
     * @param filedir
     *            文件
     * @return 大小
     */
    public static long getFileSize(File filedir) {
        long size = 0;
        if (null == filedir) {
            return size;
        }
        File[] files = filedir.listFiles();

        try {
            for (File f : files) {
                if (f.isDirectory()) {
                    size += getFileSize(f);
                } else {
                    FileInputStream fis = new FileInputStream(f);
                    size += fis.available();
                    fis.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;

    }

    /**
     * 转换文件大小
     *
     * @param fileS
     *            大小
     * @return 转换后的文件大小
     */
    public static String formatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.0");
        String fileSizeString = "";
        if (fileS == 0) {
            fileSizeString = "0" + "KB";
        } else if (fileS < SIZE_KB) {
            fileSizeString = df.format((double) fileS) + "KB";
        } else if (fileS < SIZE_MB) {
            fileSizeString = df.format((double) fileS / SIZE_KB) + "KB";
        } else if (fileS < SIZE_GB) {
            fileSizeString = df.format((double) fileS / SIZE_MB) + "M";
        } else {
            fileSizeString = df.format((double) fileS / SIZE_GB) + "G";
        }
        return fileSizeString;
    }

    /**
     * 判断SD卡是否已经准备好
     *
     * @return 是否有SDCARD
     */
    public static boolean isSDCardReady() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 文件流拷贝到文件
     *
     * @param in
     *            输入流
     * @param outFile
     *            输出文件
     * @return 操作状态
     */
    public static int copyStreamToFile(InputStream in, String outFile) {
        if (isFileExist(outFile)) {
            // 文件已经存在；
            return -2;
        }
        try {
            OutputStream fosto = new FileOutputStream(outFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = in.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            in.close();
            fosto.close();
            return 0;

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    /**
     * 将Assets目录下的APK文件拷贝到指定目录
     * @param context
     * @param fileName
     * @param path
     * @return
     */
    public static boolean copyApkFromAssets(Context context, String fileName, String path) {
        boolean copyIsFinish = false;
        try {
            InputStream is = context.getAssets().open(fileName);
            File file = new File(path);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();
            copyIsFinish = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return copyIsFinish;
    }
    /**
     * 得到sdcard路径
     *
     * @return
     */
    public static String getExtPath() {
        String path = "";
        if (isSDCardReady()) {
            path = Environment.getExternalStorageDirectory().getPath();
        }
        return path;
    }

    /**
     * 获取文件扩展名
     *
     * @param file
     * @return
     */
    public static String getFileExtension(File file) {
        if (null == file || !file.exists()) {
            return "";
        }
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }

    /**************************************/

    /**
     * 保存bitmap到SD卡
     * @param fileName
     * @param mBitmap
     */
    public static void saveMyBitmap(String fileName,Bitmap mBitmap){
        //File f = new File(Environment.getExternalStorageDirectory(), fileName);
        File f = new File(fileName);
        FileOutputStream fOut = null;
        try {
            f.createNewFile();
            fOut = new FileOutputStream(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the content:// URI  from the given corresponding path to a file
     * @param context
     * @param imageFile
     * @return content Uri
     */
    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * Gets the corresponding path to a file from the given content:// URI
     * @param selectedVideoUri The content:// URI to find the file path from
     * @param contentResolver The content resolver to use to perform the query.
     * @return the file path as a string
     */
    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePath( final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }


}