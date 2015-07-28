package com.mmh2z.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

public class HttpUtils {

	public static void setPicBitmap(final ImageView ivPic,final String pic_url){
		new Thread(new Runnable() {
			
			public void run() {
				
				try {
					HttpURLConnection conn=(HttpURLConnection) new URL(pic_url).openConnection();
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
