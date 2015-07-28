package com.mmh2z.activity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.mmh2z.adapter.CourseAdapter;
import com.mmh2z.object.Course;
import com.mmh2z.util.PullCourseService;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class MActivity extends Activity {

	private GridView gridview;
	List<Course> lists;
//	InputStream inputStream = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.init_xml);

		gridview = (GridView) findViewById(R.id.gridView1);

		lists=new ArrayList<Course>();
		
		getCourseLists();
		for(Course course:lists){
			Log.i("--", course.getId() +"  "+course.getName().toString()+"  "+course.getPicurl()+"  "+course.getState()+"  "+course.getCourseurl());
		}
		CourseAdapter adapter = new CourseAdapter(lists, this);
		
		gridview.setAdapter(adapter);
		
		gridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
			}
		});
	}
	
	//获得配置文件信息
	private void getCourseLists() {

		Thread thread = new Thread(new Runnable() {
			public void run() {
//				Log.i("-----", "1qqqq");
				AssetManager asset=getAssets();
				InputStream input = null;
				try {
					input = asset.open("course_xml.xml"); //获取配置文件
				} catch (IOException e) {
					e.printStackTrace();
				}
					List<Course> list = PullCourseService
							.getXmlCourses(input);
					if (list != null) {
						lists = list;
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
}
