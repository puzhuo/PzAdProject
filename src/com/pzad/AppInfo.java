package com.pzad;

public class AppInfo {
	
	public String name;
	public String size;
	public String icon;
	public String description;
	public String detailLink;
	public String downloadLink;
	
	@Override
	public String toString() {
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
