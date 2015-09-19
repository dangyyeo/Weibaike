package com.mmh2z.adapter;

import java.util.List;

import com.mmh2z.activity.R;
import com.mmh2z.object.TopCourse;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TopCourseAda extends BaseAdapter {

	private Context context;
	private List<TopCourse> lists;
	private int selectposition=-1;
	
	
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
		
		if(selectposition==position){
			textview.setTextColor(Color.BLUE);
		}else{
			textview.setTextColor(Color.BLACK);
		}
		return convertView;
	}
	
	public void setSelectposition(int position) {
		this.selectposition = position;
	}

}
