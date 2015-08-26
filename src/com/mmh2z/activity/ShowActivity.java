package com.mmh2z.activity;

import java.lang.reflect.Field;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewConfiguration;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ShowActivity extends Activity {

	private WebView webview;
	private ProgressDialog dialog = null;
	private String devbaseURL="http://mhbb.mhedu.sh.cn:8080/hdwiki/index.php";
//	private String devbaseURL = "http://10.106.3.106/HDWiki/index.php";
	private int cid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_web);

		ActionBar bar = getActionBar();
		bar.setDisplayHomeAsUpEnabled(true);

		webview = (WebView) findViewById(R.id.web_view);

		// 新页面接收数据
		Bundle bundle = this.getIntent().getExtras();
		cid = bundle.getInt("cid");
		String title = bundle.getString("title");

		bar.setTitle(title);

		WebSettings ws = webview.getSettings();
		ws.setJavaScriptEnabled(true);
		ws.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		// ws.setSupportZoom(true);
		// ws.setBuiltInZoomControls(true);
		// ws.setUseWideViewPort(true);
		// ws.setLoadWithOverviewMode(true);
		ws.setPluginState(PluginState.ON);
		ws.setAllowFileAccess(true);
		ws.setLoadsImagesAutomatically(true);
		
		ws.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

		webview.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			// 缓冲提示
			@Override
			public void onPageFinished(WebView view, String url) {
				dialog.dismiss();
			}

			// 加载错误时，显示界面
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				// ??
				view.loadUrl("");
				super.onReceivedError(view, errorCode, description, failingUrl);
			}
		});

		webview.setWebChromeClient(new WebChromeClient());

		webview.setOnKeyListener(new OnKeyListener() {

			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				if ((arg1 == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
					webview.goBack();
					return true;
				}
				return false;
			}
		});

		loadUrl(devbaseURL + "?app-category_view-" + cid);

		setOverflowShowingAlways();
	}

	// 后退事件
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			ShowActivity.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void loadUrl(String url) {
		if (webview != null) {
			webview.loadUrl(url);
			dialog = ProgressDialog.show(this, null, "页面加载中，请稍后..");
			webview.reload();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.show_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case R.id.ref:
			webview.reload();
			return true;
		case android.R.id.home:
			ShowActivity.this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void setOverflowShowingAlways() {
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			menuKeyField.setAccessible(true);
			menuKeyField.setBoolean(config, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
