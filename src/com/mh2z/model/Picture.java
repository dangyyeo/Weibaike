package com.mh2z.model;

public class Picture {

	private int imageid;
	private String title;

	public Picture() {
		super();
	}

	public Picture(String title,int imageid) {
		super();
		this.imageid = imageid;
		this.title = title;
	}

	public int getImageid() {
		return imageid;
	}

	public void setImageid(int imageid) {
		this.imageid = imageid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
