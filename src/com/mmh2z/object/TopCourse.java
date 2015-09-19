package com.mmh2z.object;

import java.io.Serializable;

public class TopCourse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int cid;
	private int pid;
	private String name;

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public TopCourse(int cid, int pid, String name) {
		super();
		this.cid = cid;
		this.pid = pid;
		this.name = name;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/*
	 * public int describeContents() { // TODO Auto-generated method stub return
	 * 0; }
	 * 
	 * public void writeToParcel(Parcel dest, int flags) { dest.writeInt(cid);
	 * dest.writeInt(pid); dest.writeString(name);
	 * 
	 * }
	 */
}
