package com.caro.smartmodule.base;

import android.content.Context;

/**
 * Created by caro on 2017/12/9.
 */

public class ContextHolder {
    static Context ApplicationContext;
    public static void initial(Context context) {
        ApplicationContext = context;
    }
    public static Context getContext() {
        return ApplicationContext;
    }

}
