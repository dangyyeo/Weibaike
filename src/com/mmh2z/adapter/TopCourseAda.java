package com.mmh2z.adapter;

import java.util.List;

import com.mmh2z.activity.R;
import com.mmh2z.object.TopCourse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TopCourseAda extends BaseAdapter {

	private Context context;
	private List<TopCourse> lists;
	
	
	public TopCourseAda(Context context, List<TopCourse> lists) {
		super();
		this.context = context;
		this.lists = lists;
	}

	public int getCount() {
		return lists.size();
	}

	public Object getItem(int position) {
		return lists.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.top_item, null);
		}
		TextView textview=(TextView) convertView.findViewById(R.id.Top_name);
		textview.setText(lists.get(position).getName());
		return convertView;
	}

}
