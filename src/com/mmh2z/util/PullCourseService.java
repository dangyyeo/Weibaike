package com.mmh2z.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.util.Log;
import android.util.Xml;

import com.mmh2z.object.Course;

public class PullCourseService {

	public static List<Course> getCourses(InputStream instream) {
		List<Course> courses = null;
		Course course = null;
		try {
			// 获得pull解析器
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(instream, "utf-8");
//			int depth=parser.getDepth();
			
			int eventType = parser.getEventType();
			Log.i("-----", "1qqqqaa");
			// 判断文件是否是文件的结尾，END_DOCUMENT文件结尾常量
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:// 文件开始，START_DOCUMENT文件开始开始常量
					Log.i("-----", "开始文件");
					/*String name1 = parser.getName();
					Log.i("-----+++", name1);*/
					courses = new ArrayList<Course>();
					break;

				case XmlPullParser.START_TAG:// 元素标签开始，START_TAG标签开始常量
					Log.i("-----", "开始标签");
					String name = parser.getName();
					Log.i("-----+++", name);
					if ("course".equals(name)) {
						course = new Course();
						course.setId(Integer.valueOf(parser
								.getAttributeValue(0)));
						System.out.println("id=   "
								+ parser.getAttributeValue(0));
						Log.i("--aaaa", parser.getAttributeValue(0));
					}

					if ("name".equals(name))
						course.setName(parser.nextText());
					if ("courseurl".equals(name))
						course.setCourseurl(parser.nextText());
					if ("picurl".equals(name))
						course.setPicurl(parser.nextText());
					if ("state".equals(parser.nextText()))
						course.setState(Integer.valueOf(parser.nextText()));
					
					break;
					
				case XmlPullParser.END_TAG:
					if ("course".equals(parser.getName()) && course != null) {
						courses.add(course);
						course = null;
					}
					break;
				}
				// 获取当前元素标签的类型
				Log.i("-----", "1qqqqbb");
				eventType = parser.next();
				Log.i("-----", "1qqqqcc");
			}
			instream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return courses;

	}
}
