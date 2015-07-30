package com.mmh2z.activity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.mmh2z.adapter.CourseAdapter;
import com.mmh2z.object.Course;
import com.mmh2z.util.GetJsonUtils;
import com.mmh2z.util.PullCourseService;

public class MainActivity extends Activity {

	private GridView gridview;

	private String devbaseURL = "http://192.168.1.106/hdwiki/index.php";
	private List<Course> courselist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		gridview = (GridView) findViewById(R.id.gridView);

		courselist = new ArrayList<Course>();

		// 获取已发布的Json数据，并处理之，存到courseList中
		getCourseList();

		/*if(courselist==null)
			Toast.makeText(this, "已加载所有发布微课程。。", Toast.LENGTH_SHORT).show();*/
		
		final CourseAdapter adapter = new CourseAdapter(courselist, this);
		gridview.setAdapter(adapter);

		gridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {

				final Course itemcourse = courselist.get(position);
				// saveCourseInfo(itemcourse);// 加载该课程

				new AlertDialog.Builder(MainActivity.this)
						.setTitle("已发布微课程")
						.setMessage("是否下载？")
						.setPositiveButton("下载",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {

										saveCourseInfo(itemcourse);// 加载该课程

										courselist.remove(position);// 从已发布课程中移除

										adapter.notifyDataSetChanged();//
										
										Toast.makeText(getApplicationContext(), "该课程已下载。", 0).show();

									}
								}).setNegativeButton("取消", null).show();

			}
		});
	}

	private void getCourseList() {
		Thread thread = new Thread(new Runnable() {

			public void run() {

				try {

					List<Course> list1 = getPreferinfo();// 获取配置文件信息

					List<Course> list2 = GetJsonUtils.getJsonData(devbaseURL
							+ "?app-get_course_list", "GET");
					for (Course co : list2)
						Log.i("list2__------", co.getName());
					// courselist=list2;
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
			if (flag)
				courselist.add(courseB);
		}
	}

	// 写数据到data/data中
	private void saveCourseInfo(final Course course) {

		Thread threadsa = new Thread(new Runnable() {

			public void run() {
				try {

					FileInputStream input = MainActivity.this
							.openFileInput("course_xml");
					List<Course> list2 = PullCourseService.getXmlCourses(input);// 获取配置文件信息
					for (Course co : list2)
						Log.i("peizhi____---", co.getName());
					list2.add(course);// 添加课程

					FileOutputStream outstream = MainActivity.this
							.openFileOutput("course_xml", Context.MODE_PRIVATE);

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
		FileInputStream input = null;
		try {
			input = MainActivity.this.openFileInput("course_xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<Course> list = PullCourseService.getXmlCourses(input);// 获取配置文件信息

		return list;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
