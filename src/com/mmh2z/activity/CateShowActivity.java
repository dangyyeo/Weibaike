package com.mmh2z.activity;

import java.util.ArrayList;
import java.util.List;

import com.mmh2z.adapter.TopCourseAda;
import com.mmh2z.object.TopCourse;
import com.mmh2z.util.GetTop_JsonUtils;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class CateShowActivity extends Activity implements OnItemClickListener {

	private ListView mListView;
	private List<TopCourse> course_list;
	private TopCourseAda course_adapter;
	private int oriCid;
	private String title;
	
	private String devbaseURL = "http://mhbb.mhedu.sh.cn:8080/hdwiki/index.php";

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cate_xml);

		ActionBar bar = getActionBar();
		bar.setDisplayHomeAsUpEnabled(false);
		
		mListView = (ListView) findViewById(R.id.cate_lv);
		course_list = new ArrayList<TopCourse>();

		// 新页面接收数据
		Bundle bundle = this.getIntent().getExtras();
		oriCid = bundle.getInt("cid");
		title = bundle.getString("title");
		
		bar.setTitle(title);
		
//		new MyShowCourseAsyncTask().execute();
		course_list = (List<TopCourse>) bundle.getSerializable("course");
		
		course_adapter = new TopCourseAda(CateShowActivity.this, course_list);

		mListView.setAdapter(course_adapter);
		
		mListView.setOnItemClickListener(this);

	}

	class MyShowCourseAsyncTask extends AsyncTask<Void, Void, List<TopCourse>> {

		@Override
		protected List<TopCourse> doInBackground(Void... params) {
			List<TopCourse> list = GetTop_JsonUtils.getTop_JsonData(devbaseURL
					+ "?app-get_sub_cate-" + oriCid, "GET");

			if (list != null) {
				course_list.clear();
				course_list.addAll(list);
			}
			return course_list;
		}

		@Override
		protected void onPostExecute(List<TopCourse> result) {
			super.onPostExecute(result);

			course_list = result;

			course_adapter = new TopCourseAda(CateShowActivity.this, course_list);

			mListView.setAdapter(course_adapter);

		}

	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		TopCourse item_course = course_list.get(position);
		int item_cid = item_course.getCid();
		String title = item_course.getName();
		
		Intent intent = new Intent(CateShowActivity.this,ShowActivity.class);
		
		Bundle bundle = new Bundle();
		bundle.putInt("cid", item_cid);
		bundle.putString("title", title);
		
		intent.putExtras(bundle);
		startActivity(intent);
	}
}
