package com.mmh2z.adapter;

import java.util.ArrayList;
import java.util.List;

import com.mmh2z.activity.R;
import com.mmh2z.object.Course;
import com.mmh2z.object.ViewHolder;
import com.mmh2z.util.HttpUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CourseAdapter extends BaseAdapter {

	private Context context;
	private List<Course> course;

	
	public CourseAdapter() {
		super();
		course=new ArrayList<Course>();
	}

	public CourseAdapter(List<Course> course, Context context) {
		super();
		this.context=context;
		this.course=course;
	}

	public int getCount() {
		if (null != course)
			return course.size();
		else
			return 0;
	}

	public Object getItem(int position) {
		return course.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.picture_item, null);
			viewHolder = new ViewHolder();
			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
			viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.title.setText(course.get(position).getName());

		String imageurl=course.get(position).getPicurl();
		HttpUtils.setPicBitmap(viewHolder.image, imageurl);
		
		return convertView;
	}
}
