package com.mmh2z.activity;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import android.R.bool;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.mmh2z.adapter.CourseAdapter;
import com.mmh2z.object.Course;
import com.mmh2z.util.HttpUtils;
import com.mmh2z.util.NetState;
import com.mmh2z.util.PullCourseService;

public class MActivity extends Activity {

	private GridView gridview;
	private List<Course> lists;

	private CourseAdapter adapter;
	private boolean isShowDelete = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid_view);

		Log.i("onCreate", "chenggong-----");
		gridview = (GridView) findViewById(R.id.gridView);

		lists = new ArrayList<Course>();

		getCourseLists();

		// 增加"+"选项
		addPlusOption();

		adapter = new CourseAdapter(lists, this);

		gridview.setAdapter(adapter);

		// 设置点击事件监听器
		gridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Course itemCOur = lists.get(position);

				//
				int cid = itemCOur.getCid();
				if (cid == -123) {

					boolean flag = true;
					ConnectivityManager manager = (ConnectivityManager) MActivity.this
							.getSystemService(Context.CONNECTIVITY_SERVICE);
					NetworkInfo gprs = manager
							.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
					NetworkInfo wifi = manager
							.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
					if (gprs != null) {
						if (!gprs.isConnected() && !wifi.isConnected()) {
							flag = false;
						}
					} else if (wifi != null) {
						if (!wifi.isConnected()) {
							flag = false;
						}
					}

					if (!flag) {
						Toast.makeText(MActivity.this, "请连接网络..",
								Toast.LENGTH_LONG).show();
					} else {
						// 添加课程
						Intent intent = new Intent(MActivity.this,
								MainActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
						startActivity(intent);
						MActivity.this.finish();
						overridePendingTransition(android.R.anim.fade_in,
								android.R.anim.fade_out);
					}
				} else {
					// 浏览信息
					Intent intent = new Intent(MActivity.this,
							ShowActivity.class);

					String name = itemCOur.getName();
					// 用Bundle携带数据
					Bundle bundle = new Bundle();
					bundle.putInt("cid", cid);
					bundle.putString("title", name);
					intent.putExtras(bundle);

					startActivity(intent);
				}
			}
		});

		// 设置长按事件监听器
		gridview.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (isShowDelete) {
					isShowDelete = false;
				} else {
					isShowDelete = true;
				}
				adapter.setIsShowDelete(isShowDelete);
				return true;
			}
		});

		// 检测网络状态
		CheckNetStatus();

	}

	private void CheckNetStatus() {

		NetState receiver = new NetState();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		this.registerReceiver(receiver, filter);
		receiver.onReceive(this, null);
	}

	// 增加"+"号 选项
	private void addPlusOption() {
		Course course = new Course();

		course.setId(-1);
		course.setCid(-123);// 定值
		course.setPicurl("picurl");
		course.setName("+");
		course.setState(1);

		Boolean flag = true;
		for (Course list : lists) {
			int cid = list.getCid();
			if (cid == -123) {
				flag = false;
				break;
			}
		}
		if (flag)
			lists.add(course);
	}

	// 获得data/data中相应信息
	private void getCourseLists() {

		Thread thread = new Thread(new Runnable() {
			public void run() {

				FileInputStream input = HttpUtils
						.getFileInputStr(MActivity.this); // 获取配置文件

				lists = PullCourseService.getXmlCourses(input);

			}
		});
		thread.start();

		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/*
	 * @Override protected void onResume() { Log.i("onresume",
	 * "chenggong-----"); adapter.notifyDataSetChanged(); super.onResume(); }
	 * 
	 * @Override protected void onStart() { Log.i("onStart", "chenggong-----");
	 * super.onStart(); }
	 * 
	 * @Override protected void onPause() { Log.i("onPause", "chenggong-----");
	 * super.onPause(); }
	 * 
	 * @Override protected void onStop() { Log.i("onStop", "chenggong-----");
	 * super.onStop(); }
	 * 
	 * @Override protected void onDestroy() { Log.i("onDestroy",
	 * "chenggong-----"); super.onDestroy(); }
	 * 
	 * @Override protected void onRestart() { Log.i("onRestart",
	 * "chenggong-----"); super.onRestart(); }
	 */

	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
