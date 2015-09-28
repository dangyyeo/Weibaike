package com.mmh2z.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URLDecoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewConfiguration;
import android.webkit.DownloadListener;
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
	// private String devbaseURL = "http://192.168.1.106/HDWiki/index.php";
	private int cid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_web);

		initView();

		initEvent();

		setOverflowShowingAlways();
	}

	private void initView() {
		ActionBar bar = getActionBar();
		bar.setDisplayHomeAsUpEnabled(false);

		webview = (WebView) findViewById(R.id.web_view);

		// 新页面接收数据
		Bundle bundle = this.getIntent().getExtras();
		cid = bundle.getInt("cid");
		String title = bundle.getString("title");

		bar.setTitle(title);

		WebSettings ws = webview.getSettings();
		ws.setJavaScriptEnabled(true);
		ws.setCacheMode(WebSettings.LOAD_DEFAULT);

		ws.setPluginState(PluginState.ON);
		ws.setAllowFileAccess(true);
		ws.setLoadsImagesAutomatically(true);

		ws.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

		webview.loadUrl(devbaseURL + "?app-category_view-" + cid);

	}

	private void initEvent() {

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
//				view.loadUrl("");
				String data = "无网络！请连接网络后刷新!";
		        view.loadUrl("javascript:document.body.innerHTML=\"" + data + "\"");

				/*Toast toast = Toast.makeText(ShowActivity.this,
						"请连接网络... ———刷新。", Toast.LENGTH_LONG);
				// 可以控制toast显示的位置
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				super.onReceivedError(view, errorCode, description, failingUrl);*/
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

		webview.setDownloadListener(new MyDownload());
	}

	class MyDownload implements DownloadListener {

		public void onDownloadStart(String url, String userAgent,
				String contentDisposition, String mimetype, long contentLength) {

			System.out.println("url ------------>" + url);
			if (url.endsWith(".pdf") || url.endsWith(".gif")
					|| url.endsWith(".jpg") || url.endsWith(".doc")
					|| url.endsWith(".docx") || url.endsWith(".ppt")) {
				DownloaderTask task = new DownloaderTask();
				task.execute(url);
			}
		}
	}

	// 下载内部类
	@SuppressLint("DefaultLocale")
	private class DownloaderTask extends AsyncTask<String, Void, String> {

		public DownloaderTask() {

		}

		@SuppressWarnings("deprecation")
		@Override
		protected String doInBackground(String... params) {

			String url = params[0];

			// Log.i("tag", "url="+url);
			String fileName = url.substring(url.lastIndexOf("/") + 1);

			// 文件名字如果是中文则解码
			fileName = URLDecoder.decode(fileName);

			Log.i("tag", "fileName=" + fileName);

			// 得到目录
			File directory = Environment.getExternalStorageDirectory();

			File file = new File(directory, fileName);
			// 若已经下载
			if (file.exists()) {
				Log.i("tag", "The file has already exists.");

				return fileName;
			}

			try {
				HttpClient client = new DefaultHttpClient();
				// 设置超时
				// client.getParams().setIntParameter("http.socket.timeout",3000);
				HttpGet get = new HttpGet(url);

				HttpResponse response = client.execute(get);

				if (HttpStatus.SC_OK == response.getStatusLine()
						.getStatusCode()) {
					HttpEntity entity = response.getEntity();
					InputStream input = entity.getContent();

					writeToSDCard(fileName, input);

					input.close();
					// entity.consumeContent();
					return fileName;
				} else {
					return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		// 把文件写到SD卡里面
		private void writeToSDCard(String fileName, InputStream input) {

			File directory = Environment.getExternalStorageDirectory();
			File file = new File(directory, fileName);
			if (file.exists()) {
				Log.i("tag", "The file has already exists.");
				return;
			}
			// 写入文件
			try {
				FileOutputStream fos = new FileOutputStream(file);
				byte[] b = new byte[2048];
				int j = 0;
				while ((j = input.read(b)) != -1) {
					fos.write(b, 0, j);
				}
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		@Override
		protected void onCancelled() {

			super.onCancelled();
		}

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);
			if (result == null) {
				Toast t = Toast.makeText(ShowActivity.this, "连接错误！请稍后再试！",
						Toast.LENGTH_LONG);
				t.setGravity(Gravity.CENTER, 0, 0);
				t.show();
				return;
			}

			Toast t = Toast.makeText(ShowActivity.this, "已保存到SD卡。",
					Toast.LENGTH_LONG);
			t.setGravity(Gravity.CENTER, 0, 0);
			t.show();
			File directory = Environment.getExternalStorageDirectory();
			File file = new File(directory, result);
			Log.i("tag", "Path=" + file.getAbsolutePath());

			Intent intent = getFileIntent(file);

			startActivity(intent);
		}

		private Intent getFileIntent(File file) {

			Uri uri = Uri.fromFile(file);

			String type = getMIMEType(file);

			Log.i("tag", "type=" + type);
			Intent intent = new Intent("android.intent.action.VIEW");
			intent.addCategory("android.intent.category.DEFAULT");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(uri, type);
			return intent;
		}

		private String getMIMEType(File file) {

			String type = "";
			String fName = file.getName();
			/* 取得扩展名 */
			String end = fName.substring(fName.lastIndexOf(".") + 1,
					fName.length()).toLowerCase();

			/* 依扩展名的类型决定MimeType */
			if (end.equals("pdf")) {
				type = "application/pdf";//
			} else if (end.equals("m4a") || end.equals("mp3")
					|| end.equals("mid") || end.equals("xmf")
					|| end.equals("ogg") || end.equals("wav")) {
				type = "audio/*";
			} else if (end.equals("3gp") || end.equals("mp4")) {
				type = "video/*";
			} else if (end.equals("jpg") || end.equals("gif")
					|| end.equals("png") || end.equals("jpeg")
					|| end.equals("bmp")) {
				type = "image/*";
			} else if (end.equals("apk")) {
				/* android.permission.INSTALL_PACKAGES */
				type = "application/vnd.android.package-archive";
			} else if (end.equals("pptx") || end.equals("ppt")) {
				type = "application/vnd.ms-powerpoint";
			} else if (end.equals("docx") || end.equals("doc")) {
				type = "application/vnd.ms-word";
			} else if (end.equals("xlsx") || end.equals("xls")) {
				type = "application/vnd.ms-excel";
			} else {
				/* 如果无法直接打开，就跳出软件列表给用户选择 */
				type = "*/*";
			}
			return type;
		}

		@Override
		protected void onPreExecute() {

			super.onPreExecute();

		}

		@Override
		protected void onProgressUpdate(Void... values) {

			super.onProgressUpdate(values);
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
//			webview.loadUrl(devbaseURL + "?app-category_view-" + cid);
			// Log.i("----", "xhaaaa");
			webview.reload();
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
