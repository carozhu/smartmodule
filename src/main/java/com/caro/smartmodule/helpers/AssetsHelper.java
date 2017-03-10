package com.caro.smartmodule.helpers;
import android.content.Context;
import android.content.res.AssetManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/4/6.
 */
public class AssetsHelper {

    public static byte[] getAssetsImageByte(Context context, String fileName){

        byte[] image;
        AssetManager am = context.getResources().getAssets();
        try
        {
            InputStream is = am.open(fileName);
            image = InputStreamToByte(is);
            is.close();
            return image;
        }catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    private static byte[] InputStreamToByte(InputStream is) throws IOException {
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        int ch;
        while ((ch = is.read()) != -1) {
            bytestream.write(ch);
        }
        byte imgdata[] = bytestream.toByteArray();
        bytestream.close();
        return imgdata;
    }
}