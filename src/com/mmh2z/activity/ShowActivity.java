package com.mmh2z.activity;

import java.lang.reflect.Field;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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
import android.widget.Toast;

public class ShowActivity extends Activity {

	private WebView webview;
	private ProgressDialog dialog;
	private String devbaseURL = "http://mhbb.mhedu.sh.cn:8080/hdwiki/index.php";
	// private String devbaseURL = "http://10.106.3.106/HDWiki/index.php";
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

			// 加载错误时，显示界面
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				// ??
				view.loadUrl("");
				Toast toast = Toast.makeText(ShowActivity.this, "请连接网络.. —刷新。",
						Toast.LENGTH_LONG);
				// 可以控制toast显示的位置
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				super.onReceivedError(view, errorCode, description, failingUrl);
			}
		});

		webview.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// newProgress 1~100整数
				if (newProgress == 100) {
					// 网页加载完毕，关闭
					closeDialog();
				} else {
					// 网页正在加载，打开ProgressDialog
					openDialog();
				}
			}

			private void openDialog() {
				if (dialog == null) {
					dialog = new ProgressDialog(ShowActivity.this);
					dialog = ProgressDialog.show(ShowActivity.this, null,
							"页面加载中，请稍后..");
				}
			}

			private void closeDialog() {
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
					dialog = null;
				}
			}
		});

		webview.setOnKeyListener(new OnKeyListener() {

			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				if ((arg1 == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
					webview.goBack();
					return true;
				}
				return false;
			}
		});

		webview.loadUrl(devbaseURL + "?app-category_view-" + cid);

		setOverflowShowingAlways();
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
//			Log.i("----", "xhaaaa");
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
