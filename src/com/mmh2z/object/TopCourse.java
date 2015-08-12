package com.mmh2z.object;

public class TopCourse {

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
}
