package com.pzad.entities;

import java.io.Serializable;

public class BannerInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3979688331313350236L;
	private String name;
	private String picture;
	private String link;
	
	
	
	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getPicture() {
		return picture;
	}



	public void setPicture(String picture) {
		this.picture = picture;
	}



	public String getLink() {
		return link;
	}



	public void setLink(String link) {
		this.link = link;
	}



	@Override
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("name = ").append(name)
		      .append("; picture = ").append(picture)
		      .append("; link = ").append(link);
		
		return buffer.toString();
	}
}
