package com.mmh2z.activity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mmh2z.adapter.CourseAdapter;
import com.mmh2z.adapter.TopCourseAda;
import com.mmh2z.object.Course;
import com.mmh2z.object.TopCourse;
import com.mmh2z.util.GetJsonUtils;
import com.mmh2z.util.GetTop_JsonUtils;
import com.mmh2z.util.HttpUtils;
import com.mmh2z.util.PullCourseService;

public class MainActivity extends Activity {

	private GridView gridview;
	private ListView mlistview;
	private TextView tvall;

	private String devbaseURL="http://mhbb.mhedu.sh.cn:8080/hdwiki/index.php";
//	private String devbaseURL = "http://192.168.1.106/hdwiki/index.php";
//	private String devbaseURL = "http://10.106.3.106/hdwiki/index.php";
	private List<Course> courselist;
	private List<TopCourse> toplist;

	private CourseAdapter adapter;
	private AlertDialog dialog = null;
	Boolean flag = false;

	private DrawerLayout drawer;

	private ActionBar bar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);

		bar = getActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
		
		tvall = (TextView) findViewById(R.id.tv_all);

		gridview = (GridView) findViewById(R.id.gridView);

		// 初始化侧滑分类列表
		mlistview = (ListView) findViewById(R.id.lv_list);
		drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

		toplist = new ArrayList<TopCourse>();
		initTopList();
		TopCourseAda topadapter = new TopCourseAda(this, toplist);
		mlistview.setAdapter(topadapter);

		// 获取已发布的Json数据，并处理之，存到courseList中
		courselist = new ArrayList<Course>();

		getCourseList();

		adapter = new CourseAdapter(courselist, this);

		gridview.setAdapter(adapter);

		if (courselist == null)
			Toast.makeText(getApplicationContext(), "无发布课程。", 0).show();

		gridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {

				final Course itemcourse = courselist.get(position);
				String name=itemcourse.getName();
				dialog = new AlertDialog.Builder(MainActivity.this)
						.setTitle(name)
						.setMessage("是否添加该课程？")
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

		tvall.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				
				// 清空courselist列表
				List<Course> newlist = new ArrayList<Course>();
				if (courselist != null)
					for (Course list : courselist) {
						newlist.add(list);
					}
				courselist.removeAll(newlist);
				
				// 获得courselist列表
				getCourseList();
				adapter.notifyDataSetChanged();
				
				drawer.closeDrawer(Gravity.LEFT);
				bar.setTitle("已发布课程");
			}
		});

		mlistview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// 清空courselist列表
				List<Course> newlist = new ArrayList<Course>();
				if (courselist != null)
					for (Course list : courselist) {
						newlist.add(list);
					}
				courselist.removeAll(newlist);

				// 获得courselist列表
				getCourseList();

				// 获得选中分类cid
				TopCourse topItem = toplist.get(position);
				int cid = topItem.getCid();
				String name=topItem.getName();
				UpdateCourseLists(cid);

				drawer.closeDrawer(Gravity.LEFT);
				
				bar.setTitle(name);
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

	private void initTopList() {

		Thread th = new Thread(new Runnable() {

			public void run() {

				List<TopCourse> list = GetTop_JsonUtils.getTop_JsonData(
						devbaseURL + "?app-get_topcourse_list", "GET");

				if (list != null) {
					toplist = list;
				}
			}
		});
		th.start();
		try {
			th.join();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void getCourseList() {

		Thread thread = new Thread(new Runnable() {

			public void run() {

				try {

					List<Course> list1 = getPreferinfo();// 获取配置文件信息

					List<Course> list2 = GetJsonUtils.getJsonData(devbaseURL
							+ "?app-get_course_list", "GET");

					dealList(list1, list2); // 处理List<Course>信息

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
							.getFileInputStr(MainActivity.this);

					List<Course> list2 = PullCourseService.getXmlCourses(input);// 获取配置文件信息
					/*
					 * for (Course co : list2) Log.i("peizhi____---",
					 * co.getName());
					 */
					list2.add(0, course);// 添加课程

					FileOutputStream outstream = HttpUtils
							.getFileOutputStr(MainActivity.this);

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

		FileInputStream input = HttpUtils.getFileInputStr(MainActivity.this);

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
			startActivity(new Intent(MainActivity.this, MActivity.class));
			MainActivity.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case R.id.action_settings:
			if (drawer.isDrawerOpen(Gravity.LEFT))
				drawer.closeDrawer(Gravity.LEFT);
			else
				drawer.openDrawer(Gravity.LEFT);
			return true;
		case android.R.id.home:
			startActivity(new Intent(MainActivity.this, MActivity.class));
			MainActivity.this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
