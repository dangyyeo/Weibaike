package com.mmh2z.activity;

import java.util.ArrayList;
import java.util.List;

import com.mmh2z.activity.R;
import com.mmh2z.adapter.CourseAdapter;
import com.mmh2z.object.Course;
import com.mmh2z.util.GetJsonUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

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

		// 添加
		// courselist.add(object)

		System.out.println("_______________------");
		CourseAdapter adapter = new CourseAdapter(courselist, this);
		gridview.setAdapter(adapter);

		gridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// Picture item_select=
				// if (parent.getId() == R.drawable.aa) {
				View downitem = getLayoutInflater().inflate(R.layout.list_item,
						null);
				new AlertDialog.Builder(MainActivity.this)
						.setTitle("已发布微课程")
						.setView(downitem)
						.setPositiveButton("下载",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {

									}
								}).setNegativeButton("取消", null).show();

				// } else{
				//
				// Toast.makeText(MainActivity.this, "pic" + (position + 1),
				// Toast.LENGTH_SHORT).show();
				// }
			}
		});
	}

	private void getCourseList() {
		Thread thread = new Thread(new Runnable() {

			public void run() {
				List<Course> list = GetJsonUtils.getJsonData(devbaseURL
						+ "?app-get_course_list", "GET");
				if (list != null) {
					courselist = list;
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
