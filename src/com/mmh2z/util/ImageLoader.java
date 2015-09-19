package com.mmh2z.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

public class ImageLoader {

	// 创建Cache
	private LruCache<String, Bitmap> mCaches;

	public ImageLoader() {
		// 获取最大可用内存
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory / 4;
		mCaches = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				// 在每次存入缓存时调用
				return value.getByteCount();
			}
		};
	}

	// 把图片存入缓存
	public void addBitmapToCache(String url, Bitmap bitmap) {

		if (getBitmapFromCache(url) == null) {
			mCaches.put(url, bitmap);
		}
	}

	// 从缓存中取出图片
	public Bitmap getBitmapFromCache(String url) {
		return mCaches.get(url);
	}

	private Bitmap getBitmapFromUrl(String urlString) {
		Log.i("urlStirng", urlString);
		Bitmap bitmap;
		InputStream is = null;
		try {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			is = new BufferedInputStream(conn.getInputStream());
			bitmap = BitmapFactory.decodeStream(is);
			// //转换为圆型图片
			// bitmap = toRoundBitmap(bitmap);

			conn.disconnect();
			return bitmap;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	/*
	 * private Bitmap toRoundBitmap(Bitmap bitmap) { int width =
	 * bitmap.getWidth(); int height = bitmap.getHeight(); int r = 0; // 取最短边做边长
	 * if (width < height) { r = width; } else { r = height; } // 构建一个bitmap
	 * Bitmap backgroundBm = Bitmap.createBitmap(width, height,
	 * Config.ARGB_8888); // new一个Canvas，在backgroundBmp上画图 Canvas canvas = new
	 * Canvas(backgroundBm); Paint p = new Paint(); // 设置边缘光滑，去掉锯齿
	 * p.setAntiAlias(true); RectF rect = new RectF(0, 0, r, r); //
	 * 通过制定的rect画一个圆角矩形，当圆角X轴方向的半径等于Y轴方向的半径时， // 且都等于r/2时，画出来的圆角矩形就是圆形
	 * canvas.drawRoundRect(rect, r / 2, r / 2, p); //
	 * 设置当两个图形相交时的模式，SRC_IN为取SRC图形相交的部分，多余的将被去掉 p.setXfermode(new
	 * PorterDuffXfermode(Mode.SRC_IN)); // canvas将bitmap画在backgroundBmp上
	 * canvas.drawBitmap(bitmap, null, rect, p); return backgroundBm; }
	 */
	/**
	 * 使用AsyncTask异步加载图片
	 * 
	 * @param imageView
	 * @param url
	 */
	public void showImageByAsyncTask(ImageView imageView, final String url) {
		// 从缓存中取出url对应的图片
		Bitmap bitmap = getBitmapFromCache(url);
		// 如果缓存中没有，必须从网络下载图片
		if (bitmap == null) {
			new ImageAsyncTask(imageView, url).execute(url);
		} else {
			imageView.setImageBitmap(bitmap);
		}
	}

	private class ImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

		private ImageView mImageView;
		private String mUrl;

		public ImageAsyncTask(ImageView imageView, String url) {
			mImageView = imageView;
			mUrl = url;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			String url = params[0];
			// 从网络获取图片
			Bitmap bitmap = getBitmapFromUrl(url);
			if (bitmap != null) {
				// 将不再缓存中图片，加入缓存
				addBitmapToCache(url, bitmap);
			}
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			if (mImageView.getTag().equals(mUrl)) {
				mImageView.setImageBitmap(result);
			}
		}
	}
}
