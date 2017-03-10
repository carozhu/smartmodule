package com.caro.smartmodule.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.caro.smartmodule.R;

import java.io.File;

/**
 * Description the class 打开文件帮助类
 *
 * @author zou.sq
 * @version 1.0
 */
public class OpenFileUtil {

    public static final int FILE_ENDING_IMAGE = 0;
    public static final int FILE_ENDING_AUDIO = 1;
    public static final int FILE_ENDING_VIDEO = 2;
    public static final int FILE_ENDING_PACKAGE = 3;
    public static final int FILE_ENDING_WEBTEXT = 4;
    public static final int FILE_ENDING_TEXT = 5;
    public static final int FILE_ENDING_WORD = 6;
    public static final int FILE_ENDING_EXCEL = 7;
    public static final int FILE_ENDING_PPT = 8;
    public static final int FILE_ENDING_PDF = 9;
    public static final int FILE_ENDING_APK = 10;
    public static final int FILE_ENDING_CHM = 11;

    /**
     * @param file 图片文件
     * @return Intent
     * @Description android获取一个用于打开图片文件的intent
     * @Author Administrator
     * @Date 2014年7月31日 下午6:19:44
     */
    public static Intent getImageFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    // android获取一个用于打开音频文件的intent
    public static Intent getAudioFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }

    // android获取一个用于打开视频文件的intent
    public static Intent getVideoFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "video/*");
        return intent;
    }

    // android获取一个用于打开HTML文件的intent
    public static Intent getHtmlFileIntent(File file) {
        Uri uri = Uri.parse(file.toString()).buildUpon().encodedAuthority("com.android.htmlfileprovider")
                .scheme("content").encodedPath(file.toString()).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    // android获取一个用于打开文本文件的intent
    public static Intent getTextFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "text/plain");
        return intent;
    }

    // android获取一个用于打开Word文件的intent
    public static Intent getWordFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    // android获取一个用于打开Excel文件的intent
    public static Intent getExcelFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    // android获取一个用于打开PPT文件的intent
    public static Intent getPPTFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

    // android获取一个用于打开PDF文件的intent
    public static Intent getPdfFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }

    // android获取一个用于打开CHM文件的intent
    public static Intent getChmFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    // android获取一个用于打开apk文件的intent
    public static Intent getApkFileIntent(File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        return intent;
    }

    public static Intent openFile(File file, Context context) {
        Intent intent = null;
        int fileEndingCode = getFileEnding(file, context);
        switch (fileEndingCode) {
            case FILE_ENDING_IMAGE:
                intent = getImageFileIntent(file);
                break;
            case FILE_ENDING_AUDIO:
                intent = getAudioFileIntent(file);
                break;
            case FILE_ENDING_VIDEO:
                intent = getVideoFileIntent(file);
                break;
            case FILE_ENDING_PACKAGE:
                break;
            case FILE_ENDING_WEBTEXT:
                intent = getHtmlFileIntent(file);
                break;
            case FILE_ENDING_TEXT:
                intent = getTextFileIntent(file);
                break;
            case FILE_ENDING_WORD:
                intent = getWordFileIntent(file);
                break;
            case FILE_ENDING_EXCEL:
                intent = getExcelFileIntent(file);
                break;
            case FILE_ENDING_PPT:
                intent = getPPTFileIntent(file);
                break;
            case FILE_ENDING_PDF:
                intent = getPdfFileIntent(file);
                break;
            case FILE_ENDING_APK:
                intent = getApkFileIntent(file);
                break;
            case FILE_ENDING_CHM:
                intent = getChmFileIntent(file);
                break;

            default:
                break;
        }
        return intent;

    }

    public static int getFileEnding(File file, Context context) {
        int fileEndingCode = -1;
        String[] fileEndingImage = context.getResources().getStringArray(R.array.fileEndingImage);
        String[] fileEndingAudio = context.getResources().getStringArray(R.array.fileEndingAudio);
        String[] fileEndingVideo = context.getResources().getStringArray(R.array.fileEndingVideo);
        String[] fileEndingPackage = context.getResources().getStringArray(R.array.fileEndingPackage);
        String[] fileEndingWebText = context.getResources().getStringArray(R.array.fileEndingWebText);
        String[] fileEndingText = context.getResources().getStringArray(R.array.fileEndingText);
        String[] fileEndingWord = context.getResources().getStringArray(R.array.fileEndingWord);
        String[] fileEndingExcel = context.getResources().getStringArray(R.array.fileEndingExcel);
        String[] fileEndingPPT = context.getResources().getStringArray(R.array.fileEndingPPT);
        String[] fileEndingPdf = context.getResources().getStringArray(R.array.fileEndingPdf);
        String[] fileEndingApk = context.getResources().getStringArray(R.array.fileEndingApk);
        String[] fileEndingChm = context.getResources().getStringArray(R.array.fileEndingChm);
        if (checkEndsWithInStringArray(file, fileEndingImage)) {
            fileEndingCode = FILE_ENDING_IMAGE;
        } else if (checkEndsWithInStringArray(file, fileEndingAudio)) {
            fileEndingCode = FILE_ENDING_AUDIO;
        } else if (checkEndsWithInStringArray(file, fileEndingVideo)) {
            fileEndingCode = FILE_ENDING_VIDEO;
        } else if (checkEndsWithInStringArray(file, fileEndingPackage)) {
            fileEndingCode = FILE_ENDING_PACKAGE;
        } else if (checkEndsWithInStringArray(file, fileEndingWebText)) {
            fileEndingCode = FILE_ENDING_WEBTEXT;
        } else if (checkEndsWithInStringArray(file, fileEndingText)) {
            fileEndingCode = FILE_ENDING_TEXT;
        } else if (checkEndsWithInStringArray(file, fileEndingWord)) {
            fileEndingCode = FILE_ENDING_WORD;
        } else if (checkEndsWithInStringArray(file, fileEndingExcel)) {
            fileEndingCode = FILE_ENDING_EXCEL;
        } else if (checkEndsWithInStringArray(file, fileEndingPPT)) {
            fileEndingCode = FILE_ENDING_PPT;
        } else if (checkEndsWithInStringArray(file, fileEndingPdf)) {
            fileEndingCode = FILE_ENDING_PDF;
        } else if (checkEndsWithInStringArray(file, fileEndingApk)) {
            fileEndingCode = FILE_ENDING_APK;
        } else if (checkEndsWithInStringArray(file, fileEndingChm)) {
            fileEndingCode = FILE_ENDING_CHM;
        }
        return fileEndingCode;
    }

    private static boolean checkEndsWithInStringArray(File file, String[] fileEndings) {
        if (null == file || !file.exists()) {
            return false;
        }
        for (String aEnd : fileEndings) {
            if (file.getName().endsWith(aEnd)) {
                return true;
            }
        }
        return false;
    }

}