package com.caro.smartmodule.ImageLoader;

import android.content.Context;
import android.os.Environment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.GlideModule;
import com.bumptech.glide.request.target.ViewTarget;
import com.caro.smartmodule.R;

import java.io.File;

/**
 * Created by caro on 16/6/23.
 * 自定义SmartGlideModule
 */
public class SmartGlideModule  implements GlideModule {

    // 默认存放图片的路径
    public final static String DEFAULT_SAVE_IMAGE_PATH = Environment.getExternalStorageDirectory() + File.separator + "smartmodule" + File.separator + "Images"
            + File.separator;

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // TODO: 16/6/24 Apply options to the builder here.
        //设置别的get/set tag id，以免占用View默认的
        ViewTarget.setTagId(R.id.glide_tag_id);
        //设置图片质量为高质量
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, DEFAULT_SAVE_IMAGE_PATH, 100 * 1024 * 1024));//100M


    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        // TODO: 16/6/24 register ModelLoaders here.

    }




}

