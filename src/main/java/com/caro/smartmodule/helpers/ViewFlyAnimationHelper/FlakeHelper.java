/*
 * Copyright (C) 2012 www.apkdv.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.caro.smartmodule.helpers.ViewFlyAnimationHelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;

import com.caro.smartmodule.utils.DisplayMetricsUtil;

import java.util.HashMap;

/**
 * This class represents a single Droidflake, with properties representing its
 * size, rotation, location, and speed.
 */
public class FlakeHelper {

    // These are the unique properties of any flake: its size, rotation, speed,
    // location, and its underlying Bitmap object
    float x, y;
    float rotation;
    float speed;
    float rotationSpeed;
    int width, height;
    Bitmap bitmap;

    // This map stores pre-scaled bitmaps according to the width. No reason to create
    // new bitmaps for sizes we've already seen.
    static HashMap<Integer, Bitmap> bitmapMap = new HashMap<Integer, Bitmap>();

    /**
     * Creates a new droidflake in the given xRange and with the given bitmap. Parameters of
     * location, size, rotation, and speed are randomly determined.
     */
    static FlakeHelper createFlake(float xRange, Bitmap originalBitmap, Context Context) {
        FlakeHelper flakeHelper = new FlakeHelper();
        // Size each flakeHelper with a width between 5 and 55 and a proportional height
        DisplayMetrics metrics = DisplayMetricsUtil.getDisplayMetrics(Context);
        if (metrics.widthPixels >= 1080) {
            flakeHelper.width = (int) (5 + (float) Math.random() * 80);
            float hwRatio = originalBitmap.getHeight() / originalBitmap.getWidth();
            flakeHelper.height = (int) (flakeHelper.width * hwRatio + 60);
        } else {
            flakeHelper.width = (int) (5 + (float) Math.random() * 50);
            float hwRatio = originalBitmap.getHeight() / originalBitmap.getWidth();
            flakeHelper.height = (int) (flakeHelper.width * hwRatio + 40);

        }
        // Position the flakeHelper horizontally between the left and right of the range
        flakeHelper.x = (float) Math.random() * (xRange - flakeHelper.width);
        // Position the flakeHelper vertically slightly off the top of the display
        flakeHelper.y = 0 - (flakeHelper.height + (float) Math.random() * flakeHelper.height);

        // Each flakeHelper travels at 50-200 pixels per second
        flakeHelper.speed = 50 + (float) Math.random() * 150;

        // Flakes start at -90 to 90 degrees rotation, and rotate between -45 and 45
        // degrees per second
        flakeHelper.rotation = (float) Math.random() * 180 - 90;
        flakeHelper.rotationSpeed = (float) Math.random() * 90 - 45;

        // Get the cached bitmap for this size if it exists, otherwise create and cache one
        flakeHelper.bitmap = bitmapMap.get(flakeHelper.width);
        if (flakeHelper.bitmap == null) {
            flakeHelper.bitmap = Bitmap.createScaledBitmap(originalBitmap,
                    (int) flakeHelper.width, (int) flakeHelper.height, true);
            bitmapMap.put(flakeHelper.width, flakeHelper.bitmap);
        }
        return flakeHelper;
    }
}
