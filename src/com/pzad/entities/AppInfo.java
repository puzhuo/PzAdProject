package com.pzad.entities;

import java.io.Serializable;

public class AppInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2337760721047852481L;
	
	private String name;
	private String size;
	private String icon;
	private String description;
	private String detailLink;
	private String downloadLink;
	
	
	
	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getSize() {
		return size;
	}



	public void setSize(String size) {
		this.size = size;
	}



	public String getIcon() {
		return icon;
	}



	public void setIcon(String icon) {
		this.icon = icon;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public String getDetailLink() {
		return detailLink;
	}



	public void setDetailLink(String detailLink) {
		this.detailLink = detailLink;
	}



	public String getDownloadLink() {
		return downloadLink;
	}



	public void setDownloadLink(String downloadLink) {
		this.downloadLink = downloadLink;
	}



	@Override
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("name = ").append(name)
		      .append("; size = ").append(size)
		      .append("; icon = ").append(icon)
		      .append("; description = ").append(description)
		      .append("; detailLink = ").append(detailLink)
		      .append("; downloadLink = ").append(downloadLink);
		
		return buffer.toString();
	}
}
