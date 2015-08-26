package com.mmh2z.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

import com.mmh2z.object.Course;

public class PullCourseService {

	public static List<Course> getXmlCourses(InputStream instream) {
		List<Course> courses = null;
		Course course = null;
		try {
			// 获得pull解析器
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(instream, "utf-8");

			int eventType = parser.getEventType();
			// 判断文件是否是文件的结尾，END_DOCUMENT文件结尾常量
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:// 文件开始，START_DOCUMENT文件开始常量
					courses = new ArrayList<Course>();
					break;

				case XmlPullParser.START_TAG:// 元素标签开始，START_TAG标签开始常量
					String name = parser.getName();
					if ("course".equals(name)) {
						course = new Course();
						course.setId(Integer.valueOf(parser
								.getAttributeValue(0)));
					}
					if (course != null) {
						
						if ("name".equals(name))
							course.setName(parser.nextText());
						if ("cid".equals(name))
							course.setCid(Integer.valueOf(parser.nextText()));
						if ("picurl".equals(name))
							course.setPicurl(parser.nextText());
						if ("state".equals(name))
							course.setState(Integer.valueOf(parser.nextText()));
					}

					break;

				case XmlPullParser.END_TAG:
					if ("course".equals(parser.getName()) && course != null) {
						courses.add(course);
						course = null;
					}
					break;
				}
				// 获取当前元素标签的类型
				eventType = parser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				instream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return courses;

	}
	
	public static void saveXmlCourses(List<Course> courses ,OutputStream outstream){
		
		XmlSerializer serializer=Xml.newSerializer();
		try {
			serializer.setOutput(outstream, "utf-8");
			serializer.startDocument("utf-8", true);
			
			serializer.startTag(null, "courses");//标签
			//加载数据
			for(Course course : courses){
				serializer.startTag(null, "course");
				serializer.attribute(null, "id", course.getId()+"");
				
				serializer.startTag(null, "name");
				serializer.text(course.getName());
				serializer.endTag(null, "name");
				
				serializer.startTag(null, "cid");
				serializer.text(course.getCid()+"");
				serializer.endTag(null, "cid");
				
				serializer.startTag(null, "picurl");
				serializer.text(course.getPicurl());
				serializer.endTag(null, "picurl");
				
				serializer.startTag(null, "state");
				serializer.text(course.getState()+"");
				serializer.endTag(null, "state");
				
				serializer.endTag(null, "course");
			}
			
			serializer.endTag(null, "courses");
			serializer.endDocument();
			outstream.flush();
			outstream.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
