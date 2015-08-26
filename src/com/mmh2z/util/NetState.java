package com.mmh2z.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetState extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent arg1) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo gprs = manager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifi = manager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (!gprs.isConnected() && !wifi.isConnected()) {
			Toast.makeText(context.getApplicationContext(), "网络连接断开，请检查网络..",
					Toast.LENGTH_LONG).show();
		}else{
			Toast.makeText(context.getApplicationContext(), "网络连接成功",
					Toast.LENGTH_SHORT).show();
		}
		
		context.unregisterReceiver(this);
	}
}
