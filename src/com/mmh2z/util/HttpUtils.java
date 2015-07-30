package com.mmh2z.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

public class HttpUtils {

	private static String devbaseURL = "http://192.168.1.106/hdwiki/";
	
	public static void setPicBitmap(final ImageView ivPic,final String pic_url){
		final String url=devbaseURL+pic_url;
		new Thread(new Runnable() {
			
			public void run() {
				
				try {
					HttpURLConnection conn=(HttpURLConnection) new URL(url).openConnection();
					conn.connect();
					InputStream is=conn.getInputStream();
					Bitmap bitmap=BitmapFactory.decodeStream(is);
					ivPic.setImageBitmap(bitmap);
					is.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
