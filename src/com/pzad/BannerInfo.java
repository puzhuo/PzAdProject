package com.pzad;

public class BannerInfo {

	public String name;
	public String picture;
	public String link;
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("name = ").append(name)
			.append("; picture = ").append(picture)
			.append("; link = ").append(link);
		return buffer.toString();
	}
}
