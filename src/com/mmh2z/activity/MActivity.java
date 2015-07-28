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
		
//		inputStream = this.getResources().openRawResource(R.xml.course_xml);
		
//		Log.i("-----", inputStream.toString());
		getCourseLists();
		for(Course course:lists){
			Log.i("--", course.getName().toString());
		}
//		lists=PullCourseService.getCourses(inputStream);
		CourseAdapter adapter = new CourseAdapter(lists, this);
		
		gridview.setAdapter(adapter);
		
		gridview.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
			}
		});
	}

	private void getCourseLists() {

		Log.i("-----", "1qq");

//		Thread thread = new Thread(new Runnable() {
//			public void run() {
				Log.i("-----", "1qqqq");
				AssetManager asset=getAssets();
				InputStream input = null;
				try {
					input = asset.open("course_xml.xml");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					List<Course> list = PullCourseService
							.getCourses(input);
					Log.i("-----", "1qqqqqq");
					if (list != null) {
						lists = list;
					}
					Log.i("-----", "1qqqqqq11");
//			}
//		});
//		thread.start();

		/*try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
	}
}
