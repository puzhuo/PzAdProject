package com.pzad.entities;

import java.io.Serializable;

public class Statistic implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8178010307070922374L;
	
	public static final int TYPE_BANNER = 0x1;
	public static final int TYPE_APP = 0x2;
	public static final int TYPE_WINDOW = 0x4;
	
	private int downloadCount;
	private int browseDetailCount;
	private int exhibitionCount;
	
	private String name;

	public int getDownloadCount() {
		return downloadCount;
	}

	public void setDownloadCount(int downloadCount) {
		this.downloadCount = downloadCount;
	}

	public int getBrowseDetailCount() {
		return browseDetailCount;
	}

	public void setBrowseDetailCount(int browseDetailCount) {
		this.browseDetailCount = browseDetailCount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name, int type) {
		this.name = name + ":" + type;
	}

	public int getExhibitionCount() {
		return exhibitionCount;
	}

	public void setExhibitionCount(int exhibitionCount) {
		this.exhibitionCount = exhibitionCount;
	}

	@Override
	public String toString() {
		return "Statistic [downloadCount=" + downloadCount
				+ ", browseDetailCount=" + browseDetailCount
				+ ", exhibitionCount=" + exhibitionCount + ", name=" + name
				+ "]";
	}
	
	
}
