package com.mmh2z.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class ShowActivity extends Activity{

	private WebView webview;
	private String url;
	
	public  ShowActivity(String url){
		super();
		this.url=url;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_web);
		
		webview=(WebView) findViewById(R.id.web_view);
		
		
	}
}
