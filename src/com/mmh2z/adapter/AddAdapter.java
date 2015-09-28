package com.mmh2z.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mmh2z.activity.R;
import com.mmh2z.object.Course;
import com.mmh2z.object.ViewHolder;
import com.mmh2z.util.ImageLoader;

public class AddAdapter extends BaseAdapter {

	private Context context;
	private List<Course> courses;
	private ImageLoader mImageLoader;

	private String devbaseURL = "http://mhbb.mhedu.sh.cn:8080/hdwiki/";

	public AddAdapter() {
		super();
		courses = new ArrayList<Course>();
	}

	public AddAdapter(List<Course> course, Context context) {
		super();
		this.context = context;
		this.courses = course;
		mImageLoader = new ImageLoader();
	}

	public int getCount() {
		if (null != courses)
			return courses.size();
		else
			return 0;
	}

	public Object getItem(int position) {
		return courses.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();

			convertView = LayoutInflater.from(context).inflate(
					R.layout.add_item, null);
			viewHolder.name = (TextView) convertView
					.findViewById(R.id.add_name);
			viewHolder.image = (ImageView) convertView
					.findViewById(R.id.add_image);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.name.setText(courses.get(position).getName());

		viewHolder.image.setImageResource(R.drawable.picture);
		// 动态加载图片
		String image_url = courses.get(position).getPicurl();
		if (!image_url.equals("")) {
			viewHolder.image.setTag(devbaseURL + image_url);
			mImageLoader.showImageByAsyncTask(viewHolder.image, devbaseURL
					+ image_url);
		}
		return convertView;
	}
}
