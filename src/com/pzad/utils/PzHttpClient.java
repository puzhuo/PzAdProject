package com.pzad.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class PzHttpClient {
	
	public static final int CONNECTION_TIMEOUT = 5000;
	
	private HttpClient httpClient;
	
	public PzHttpClient(){
		httpClient = new DefaultHttpClient();
	}
	
	public String httpGet(String url, List<BasicNameValuePair> params) throws Exception{
		
		if(params != null && params.size() > 0){
			return httpGet(url, URLEncodedUtils.format(params, "UTF-8"));
		}
		
		return null;
		
	}
	
	public String httpGet(String url, String queryString) throws Exception{
		String responseData = null;
        if (queryString != null && !queryString.equals("")) {
            url += "?" + queryString;
        }
        
        PLog.d("start_httpget", url);

        HttpGet httpGet = new HttpGet(url);
        httpGet.getParams().setParameter("http.socket.timeout", Integer.valueOf(CONNECTION_TIMEOUT));
        try {
            HttpResponse response = httpClient.execute(httpGet);
            responseData = EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpGet.abort();
        }
        PLog.d("http_get", responseData);

        return responseData;
	}
	
	public String httpPost(String url, String queryString) throws URISyntaxException, UnsupportedEncodingException{
		String responseData = null;
        URI tmpUri = new URI(url);
        URI uri = URIUtils.createURI(tmpUri.getScheme(), tmpUri.getHost(), tmpUri.getPort(), tmpUri.getPath(),  queryString, null);
        
        HttpPost httpPost = new HttpPost(uri);
        httpPost.getParams().setParameter("http.socket.timeout", Integer.valueOf(CONNECTION_TIMEOUT));
        if (queryString != null && !queryString.equals("")) {
            StringEntity reqEntity = new StringEntity(queryString);   
            reqEntity.setContentType("application/x-www-form-urlencoded");   
            httpPost.setEntity(reqEntity);
        }

        try {
            HttpResponse response = httpClient.execute(httpPost);
            responseData = EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            httpPost.abort();
        }
        
        PLog.d("POST_METHOD", responseData);

        return responseData;
	}
	
	public String httpPost(String url, List<BasicNameValuePair> params){
		String result = null;
		try{
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			PLog.d("post", httpPost.getURI().toString());
			InputStream is = httpClient.execute(httpPost).getEntity().getContent();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
			StringBuilder stringBuilder = new StringBuilder();
			String line = null;
			while((line = bufferedReader.readLine()) != null){
				stringBuilder.append(line + "\n");
			}
			is.close();
			result = stringBuilder.toString();
			PLog.d("POST_METHOD", result);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}
}
