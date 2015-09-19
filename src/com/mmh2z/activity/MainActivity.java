package com.mmh2z.activity;

import java.io.FileInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.mmh2z.adapter.CourseAdapter;
import com.mmh2z.object.Course;
import com.mmh2z.object.TopCourse;
import com.mmh2z.util.GetTop_JsonUtils;
import com.mmh2z.util.HttpUtils;
import com.mmh2z.util.NetState;
import com.mmh2z.util.PullCourseService;

public class MainActivity extends Activity {

	private GridView gridview;
	private List<Course> lists;
	private List<TopCourse> course_list;
	private CourseAdapter adapter;
	private boolean isShowDelete = false;

	private String devbaseURL = "http://mhbb.mhedu.sh.cn:8080/hdwiki/index.php";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid_view);

		Log.i("onCreate", "chenggong-----");
		gridview = (GridView) findViewById(R.id.gridView);

		lists = new ArrayList<Course>();
		course_list = new ArrayList<TopCourse>();
		// getCourseLists();

		new MygetCourseAsyncTask().execute();

		initEvent();

		// 检测网络状态
		CheckNetStatus();

	}

	private void initEvent() {
		// 设置点击事件监听器
		gridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Course itemCOur = lists.get(position);
				//
				int cid = itemCOur.getCid();

				if (cid == -123) {

					boolean flag = true;
					ConnectivityManager manager = (ConnectivityManager) MainActivity.this
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
						Toast.makeText(MainActivity.this, "网络连接失败，请重新连接..",
								Toast.LENGTH_LONG).show();
					} else {
						// 添加课程
						Intent intent = new Intent(MainActivity.this,
								AddActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
						startActivity(intent);
						MainActivity.this.finish();
						overridePendingTransition(android.R.anim.fade_in,
								android.R.anim.fade_out);
					}
				} else {
					course_list.clear();

					String name = itemCOur.getName();
					// 用Bundle携带数据
					Bundle bundle = new Bundle();
					bundle.putInt("cid", cid);
					bundle.putString("title", name);

					MyShowThread myThread = new MyShowThread(cid);
					myThread.start();
					try {
						myThread.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					if (course_list.size() == 0) {
						Intent intent = new Intent(MainActivity.this,
								ShowActivity.class);

						intent.putExtras(bundle);
						startActivity(intent);
					} else {
						// 浏览信息
						Intent intent = new Intent(MainActivity.this,
								CateShowActivity.class);
						bundle.putSerializable("course",
								(Serializable) course_list);
						intent.putExtras(bundle);

						startActivity(intent);
					}
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
		course.setName("");
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

	class MygetCourseAsyncTask extends AsyncTask<Void, Void, List<Course>> {

		@Override
		protected List<Course> doInBackground(Void... params) {
			FileInputStream input = HttpUtils
					.getFileInputStr(MainActivity.this); // 获取配置文件

			lists = PullCourseService.getXmlCourses(input);
			return lists;
		}

		@Override
		protected void onPostExecute(List<Course> result) {
			super.onPostExecute(result);

			lists = result;

			// 增加"+"选项
			addPlusOption();

			adapter = new CourseAdapter(result, MainActivity.this);

			gridview.setAdapter(adapter);
		}

	}

	/*
	 * class MyShowCourseAsyncTask extends AsyncTask<Integer, Void,
	 * List<TopCourse>> {
	 * 
	 * @Override protected List<TopCourse> doInBackground(Integer... params) {
	 * List<TopCourse> list = GetTop_JsonUtils.getTop_JsonData(devbaseURL +
	 * "?app-get_sub_cate-" + params[0], "GET");
	 * 
	 * return list; }
	 * 
	 * @Override protected void onPostExecute(List<TopCourse> result) {
	 * super.onPostExecute(result); if (result != null) {
	 * course_list.addAll(result); } } }
	 */

	class MyShowThread extends Thread {
		private int cid;

		public MyShowThread(int cid) {
			this.cid = cid;
		}

		@Override
		public void run() {
			List<TopCourse> list = GetTop_JsonUtils.getTop_JsonData(devbaseURL
					+ "?app-get_sub_cate-" + cid, "GET");
			if (list != null) {
				course_list.clear();
				course_list.addAll(list);
			}
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
			new MygetCourseAsyncTask().execute();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

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
