package com.mmh2z.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.mmh2z.adapter.CourseAdapter;
import com.mmh2z.object.Course;
import com.mmh2z.util.PullCourseService;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class MActivity extends Activity {

	private GridView gridview;
	List<Course> lists;

	// InputStream inputStream = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.init_xml);

		gridview = (GridView) findViewById(R.id.gridView1);

		lists = new ArrayList<Course>();
		
		getCourseLists();

		// 增加"+"选项
		addPlusOption();

		for (Course course : lists) {
			Log.i("--", course.getId() + "  " + course.getName().toString()
					+ "  " + course.getPicurl() + "  " + course.getState()
					+ "  " + course.getCid());
		}
		CourseAdapter adapter = new CourseAdapter(lists, this);

		gridview.setAdapter(adapter);

		gridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Course itemCOur = lists.get(position);

				//
				
				if (itemCOur.getId() == -1) {
					Intent intent = new Intent(MActivity.this,
							MainActivity.class);
					startActivity(intent);
				}
			}
		});
		
	}
	
	//增加"+"选项
	private void addPlusOption() {
		Course course = new Course();
		
		course.setId(-1);
		course.setCid(111);
		course.setPicurl("picurl");
		course.setName("");
		course.setState(1);
		
		lists.add(course);
	}

	// 获得data/data中相应信息
	private void getCourseLists() {

		Thread thread = new Thread(new Runnable() {
			public void run() {
				
				try {
					File file=MActivity.this.getFileStreamPath("course_xml");
					if(!file.exists())
						file.createNewFile();
					
					FileInputStream input = MActivity.this
							.openFileInput("course_xml"); // 获取配置文件
					List<Course> list = PullCourseService.getXmlCourses(input);
//					if (list != null) {
						lists = list;
//					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();

		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
