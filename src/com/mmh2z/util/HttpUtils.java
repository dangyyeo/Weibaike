package com.mmh2z.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

public class HttpUtils {

	private static String devbaseURL="http://mhbb.mhedu.sh.cn:8080/hdwiki/";
//	private static String devbaseURL = "http://10.106.3.106/hdwiki/";

	/*//设置图片
	public static void setPicBitmap(final ImageView ivPic, final String pic_url) {
		final String url = devbaseURL + pic_url;
		Thread thread = new Thread(new Runnable() {

			public void run() {

				try {
					HttpURLConnection conn = (HttpURLConnection) new URL(url)
							.openConnection();
					conn.connect();
					InputStream is = conn.getInputStream();
					Bitmap bitmap = BitmapFactory.decodeStream(is);
					ivPic.setImageBitmap(bitmap);
					is.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();

		try {
			thread.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	// 得到配置文件流FileInputStream
	public static FileInputStream getFileInputStr(Context context) {
		FileInputStream input = null;
		try {
			File file = context.getFileStreamPath("course_xml");
			if (!file.exists())
				file.createNewFile();

			input = context.openFileInput("course_xml"); // 获取配置文件
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return input;
	}

	// 得到文件流FileOuputStream
	public static FileOutputStream getFileOutputStr(Context context) {
		FileOutputStream outstream = null;
		try {
			outstream = context.openFileOutput("course_xml",
					Context.MODE_PRIVATE);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return outstream;
	}
}
