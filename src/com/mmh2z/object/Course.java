package com.mmh2z.object;

import java.io.Serializable;

public class Course implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String courseurl;
	private String picurl;
	private String name;
	private int state; // 是否发布 0未发布， 1 发布

	public Course() {
		super();
	}

	public Course(int id, String courseurl, String picurl, String name,
			int state) {
		super();
		this.id = id;
		this.courseurl = courseurl;
		this.picurl = picurl;
		this.name = name;
		this.state = state;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCourseurl() {
		return courseurl;
	}

	public void setCourseurl(String courseurl) {
		this.courseurl = courseurl;
	}

	public String getPicurl() {
		return picurl;
	}

	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}
