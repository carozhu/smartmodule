package com.caro.smartmodule.viewComponent.webview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.caro.smartmodule.R;


/**
 * @Description: 带进度条的WebView
 * @author http://blog.csdn.net/finddreams
 */ 
@SuppressWarnings("deprecation")
public class ProgressWebView extends WebView {
	public ProgressBar progressbar;
	boolean allowDragTop = true; // 如果是true，则允许拖动至底部的下一页
	float downY = 0;
	boolean needConsumeTouch = true; // 是否需要承包touch事件，needConsumeTouch一旦被定性，则不会更改

	public ProgressWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		progressbar = new ProgressBar(context, null,
				android.R.attr.progressBarStyleHorizontal);
		progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 10, 0, 0));
		Drawable drawable = context.getResources().getDrawable(R.drawable.progress_bar_states);
		progressbar.setProgressDrawable(drawable);
		addView(progressbar);
		setWebChromeClient(new WebChromeClient());
		//setWebChromeClient(new MyWebChromeHelper(progressbar));

	}

	public ProgressBar getPB(){
		return this.progressbar;
	}
	public class WebChromeClient extends android.webkit.WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if (newProgress == 100) {
				progressbar.setVisibility(GONE);
			} else {
				if (progressbar.getVisibility() == GONE)
					progressbar.setVisibility(VISIBLE);
				progressbar.setProgress(newProgress);
			}
			super.onProgressChanged(view, newProgress);
		}

	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
		lp.x = l;
		lp.y = t;
		progressbar.setLayoutParams(lp);
		super.onScrollChanged(l, t, oldl, oldt);
	}

	public void ConfigWebView(String url) {
		MyWebChromeHelper myWebChromeHelper = new MyWebChromeHelper(this.getPB());
		setWebChromeClient(myWebChromeHelper);
		WebSettings webSettings = this.getSettings();
		// 设置支持Javascript
		webSettings.setDefaultTextEncodingName("utf-8");
		webSettings.setJavaScriptEnabled(true);
		getSettings().setSupportZoom(false);
		setWebViewClient(new WebViewClient() {
			public void onPageFinished(WebView view, String url) {
				// Phandler.sendEmptyMessage(200);//bug
			}

			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
        /*
         * 提高渲染的优先级 loding 速度
		 * mWebView.getSettings().setRenderPriority(WebSettings
		 * .RenderPriority.HIGH); 使用，把图片加载放在最后来加载渲染。参照示例1.setBlockNetworkImage
		 * mWebView.getSettings().setBlockNetworkImage(false);
		 */
		final Handler handler = new Handler();
		/*addJavascriptInterface(new Object() {
			public void clickOnAndroid() {
				handler.post(new Runnable() {
					public void run() {
						loadUrl("javascript:wave()");
					}
				});
			}
		}, "demo");*/
		loadUrl(url);
	}

	/**
	 *
	 */
	public void zm(){
		getSettings().setBuiltInZoomControls(true);
		getSettings().setDomStorageEnabled(true);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			downY = ev.getRawY();
			needConsumeTouch = true; // 默认情况下，listView内部的滚动优先，默认情况下由该listView去消费touch事件
			allowDragTop = isAtTop();
		} else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
			if (!needConsumeTouch) {
				// 在最顶端且向上拉了，则这个touch事件交给父类去处理
				getParent().requestDisallowInterceptTouchEvent(false);
				return false;
			} else if (allowDragTop) {
				// needConsumeTouch尚未被定性，此处给其定性
				// 允许拖动到底部的下一页，而且又向上拖动了，就将touch事件交给父view
				if (ev.getRawY() - downY > 2) {
					// flag设置，由父类去消费
					needConsumeTouch = false;
					getParent().requestDisallowInterceptTouchEvent(false);
					return false;
				}
			}
		}

		// 通知父view是否要处理touch事件
		getParent().requestDisallowInterceptTouchEvent(needConsumeTouch);
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * 判断listView是否在顶部
	 *
	 * @return 是否在顶部
	 */
	private boolean isAtTop() {
		return getScrollY() == 0;
	}
}