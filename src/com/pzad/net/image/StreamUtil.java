package com.pzad.net.image;

import java.io.InputStream;
import java.io.OutputStream;

public class StreamUtil {
	public static void copyStream(InputStream is, OutputStream os, long length){
		final int bufferSize = 1024;
		try{
			byte[] bytes = new byte[bufferSize];
			int count = 0;
			int read = -1;
			read = is.read(bytes);
			os.write(bytes, 0, read);
			count = read;
			while((read = is.read(bytes, 0, bufferSize)) != -1){
				os.write(bytes, 0, read);
				count += read;
				//percentage = count * 1.0F / length;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
