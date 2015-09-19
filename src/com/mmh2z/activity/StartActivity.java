package com.mmh2z.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class StartActivity extends Activity{

	private final long SPLASH_LENGTH = 2000;    
	Handler handler=new Handler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_PROGRESS); //去标题栏
		setContentView(R.layout.launch_xml);
		
		handler.postDelayed(new Runnable() {
			
			public void run() {

				Intent intent=new Intent(StartActivity.this,MainActivity.class);
				startActivity(intent);
				finish();
			}
		}, SPLASH_LENGTH);
	}
}
