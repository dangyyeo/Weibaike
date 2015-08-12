package com.mmh2z.object;

import java.io.Serializable;

public class Course implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private int cid;
	private int pid;
	private String picurl;
	private String name;
	private int state; // 是否发布 0未发布， 1 发布

	public Course() {
		super();
	}

	public Course(int id, int cid, int pid, String picurl, String name,
			int state) {
		super();
		this.id = id;
		this.cid = cid;
		this.pid = pid;
		this.picurl = picurl;
		this.name = name;
		this.state = state;
	}

	
	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
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
