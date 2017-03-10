package com.caro.smartmodule.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;

/**
 * Created by caro on 16/9/3.
 */
public class BaseWebActivity extends BaseSimpleAppCompatActivity{


    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * setWebView for onKeyDown param webView
     * @param webView
     */
    public void setWebViewListenKeyBack(WebView webView) {
        this.webView = webView;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (webView != null) {
            if (keyCode == KeyEvent.KEYCODE_BACK && !(webView.canGoBack())) {
                finish();
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                webView.goBack();
                return true;
            }
        }
        return false;
    }


}
