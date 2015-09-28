package com.mmh2z.activity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.mmh2z.adapter.AddAdapter;
import com.mmh2z.object.Course;
import com.mmh2z.util.GetJsonUtils;
import com.mmh2z.util.HttpUtils;
import com.mmh2z.util.PullCourseService;

public class AddActivity extends Activity {

	private GridView gridview;

	private String devbaseURL = "http://mhbb.mhedu.sh.cn:8080/hdwiki/index.php";
	// private String devbaseURL = "http://192.168.1.106/hdwiki/index.php";
	private List<Course> courselist;

	private AddAdapter adapter;
	private AlertDialog dialog = null;
	Boolean flag = false;

	private ActionBar bar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_grid_view);
		Log.i("--Main---onCreate", "chenggong-----");

		bar = getActionBar();
		bar.setDisplayHomeAsUpEnabled(false);

		gridview = (GridView) findViewById(R.id.add_gridView);

		// 获取已发布的Json数据，并处理之，存到courseList中
		courselist = new ArrayList<Course>();

		new MyaddCourseAsyncTask().execute();

		initEvent();
	}

	@SuppressLint("ShowToast")
	private void initEvent() {
		gridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {

				final Course itemcourse = courselist.get(position);
				String name = itemcourse.getName();

				dialog = new AlertDialog.Builder(AddActivity.this)
						.setTitle(name)
						.setMessage("是否添加该课程？")
						.setIcon(R.drawable.ic_launcher)
						.setPositiveButton("添加",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {

										saveCourseInfo(itemcourse);// 加载该课程

										courselist.remove(position);// 从已发布课程中移除

										adapter.notifyDataSetChanged();//

										Toast.makeText(getApplicationContext(),
												"该课程已添加。", 0).show();

										flag = true;
									}
								}).setNegativeButton("取消", null).show();

			}
		});

	}

	// 更新选中分类的发布课程
	protected void UpdateCourseLists(int cid) {
		List<Course> newlist = new ArrayList<Course>();

		if (courselist != null)
			for (Course list : courselist) {
				int pid = list.getPid();
				if (cid != pid)
					newlist.add(list);
			}
		courselist.removeAll(newlist);

		adapter.notifyDataSetChanged();//
	}

	class MyaddCourseAsyncTask extends AsyncTask<Void, Void, List<Course>> {

		@Override
		protected List<Course> doInBackground(Void... params) {
			List<Course> list1 = getPreferinfo();// 获取配置文件信息

			List<Course> list2 = GetJsonUtils.getJsonData(devbaseURL
					+ "?app-get_course_list", "GET");

			dealList(list1, list2); // 处理List<Course>信息
			return courselist;
		}

		@Override
		protected void onPostExecute(List<Course> result) {
			super.onPostExecute(result);
			if (result.size() == 0) {
				Toast.makeText(AddActivity.this, "无发布课程..", Toast.LENGTH_LONG)
						.show();
			} else {
				courselist = result;

				adapter = new AddAdapter(result, AddActivity.this);

				gridview.setAdapter(adapter);
			}
		}

	}

	// 处理列表List<Course>信息，除去已下载..
	private void dealList(List<Course> listA, List<Course> listB) {

		for (Course courseB : listB) {
			Boolean flag = true;
			int idB = courseB.getId();
			for (Course courseA : listA) {
				int idA = courseA.getId();
				if (idB == idA) {
					flag = false;
					break;
				}
			}
			if (flag) {
				courselist.add(courseB);
			}
		}
	}

	// 写数据到data/data中
	private void saveCourseInfo(final Course course) {

		Thread threadsa = new Thread(new Runnable() {

			public void run() {
				try {

					FileInputStream input = HttpUtils
							.getFileInputStr(AddActivity.this);

					List<Course> list2 = PullCourseService.getXmlCourses(input);// 获取配置文件信息
					/*
					 * for (Course co : list2) Log.i("peizhi____---",
					 * co.getName());
					 */
					list2.add(0, course);// 添加课程

					FileOutputStream outstream = HttpUtils
							.getFileOutputStr(AddActivity.this);

					PullCourseService.saveXmlCourses(list2, outstream);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		threadsa.start();

		try {
			threadsa.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// 获取配置文件信息
	public List<Course> getPreferinfo() {

		FileInputStream input = HttpUtils.getFileInputStr(AddActivity.this);

		List<Course> list = PullCourseService.getXmlCourses(input);// 获取配置文件信息

		return list;

	}

	// 后退事件
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {

			if (!flag) {
				if (dialog != null && dialog.isShowing())
					dialog.cancel();
			}
			Intent intent = new Intent(AddActivity.this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			AddActivity.this.finish();
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) {
	 * getMenuInflater().inflate(R.menu.main, menu); return true; }
	 * 
	 * public boolean onOptionsItemSelected(MenuItem item) { int id =
	 * item.getItemId(); switch (id) {
	 * 
	 * case R.id.action_settings: courselist.clear(); new
	 * MyaddCourseAsyncTask().execute(); return true; default: return
	 * super.onOptionsItemSelected(item); } }
	 */
}
