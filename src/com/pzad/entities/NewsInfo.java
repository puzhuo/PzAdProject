package com.pzad.entities;

import java.io.Serializable;

public class NewsInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3217377169469911262L;
	
	private String title;
	private String url;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Override
	public String toString() {
		return "NewsInfo [title=" + title + ", url=" + url + "]";
	}
	
	

}
