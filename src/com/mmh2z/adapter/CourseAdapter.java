package com.mmh2z.adapter;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.mmh2z.activity.R;
import com.mmh2z.object.Course;
import com.mmh2z.object.ViewHolder;
import com.mmh2z.util.HttpUtils;
import com.mmh2z.util.ImageLoader;
import com.mmh2z.util.PullCourseService;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CourseAdapter extends BaseAdapter {

	private Context context;
	private List<Course> courses;
	private int selectposition = -1;
	private boolean isShowDelete;// 根据这个变量来判断是否显示删除图标，true是显示，false是不显示
	private ImageLoader mImageLoader;

	private String devbaseURL = "http://mhbb.mhedu.sh.cn:8080/hdwiki/";

	public CourseAdapter() {
		super();
		courses = new ArrayList<Course>();
	}

	public CourseAdapter(List<Course> course, Context context) {
		super();
		this.context = context;
		this.courses = course;
		mImageLoader = new ImageLoader();
	}

	// 是否显示删除图标
	public void setIsShowDelete(boolean isShowDelete) {
		this.isShowDelete = isShowDelete;
		notifyDataSetChanged();
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
					R.layout.picture_item, null);
			viewHolder.name = (TextView) convertView.findViewById(R.id.name);
			viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
			viewHolder.delete = (ImageView) convertView
					.findViewById(R.id.delete);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.name.setText(courses.get(position).getName());

		int cid = courses.get(position).getCid();
		// 处理“+”

		if (cid == -123) {
			viewHolder.delete.setVisibility(View.GONE);
			viewHolder.image.setImageResource(R.drawable.plu);
		} else {
			viewHolder.delete.setVisibility(isShowDelete ? View.VISIBLE
					: View.GONE);// 设置删除按钮是否显示
			viewHolder.image.setImageResource(R.drawable.picture);
		
			String image_url = courses.get(position).getPicurl();
			viewHolder.image.setTag(devbaseURL+image_url);
			mImageLoader.showImageByAsyncTask(viewHolder.image, devbaseURL
					+ image_url);
		}

		// 设置删除图标监听器
		viewHolder.delete.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				courses.remove(courses.get(position));

				notifyDataSetChanged();

				FileOutputStream output = HttpUtils.getFileOutputStr(context);

				PullCourseService.saveXmlCourses(courses, output); // 保存配置信息
			}
		});

		return convertView;
	}

	public void setSelectposition(int position) {
		this.selectposition = position;
	}
}
