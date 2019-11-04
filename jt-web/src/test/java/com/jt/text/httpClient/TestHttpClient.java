package com.jt.text.httpClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class TestHttpClient {

	/*
	 * 1.定位URL地址
	 * 2.创建http工具API
	 * 3.发起请求方式get/post
	 * 4.发起请求，获得响应response对象
	 * 5.判断服务器返回状态是否正确(200)
	 * 6.获取服务器响应数据String类型
	 */
	@Test
	public void doGet() throws ClientProtocolException, IOException {
		String url = "https://www.baidu.com";
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet get = new HttpGet(url);
		CloseableHttpResponse response = client.execute(get);
		if(response.getStatusLine().getStatusCode() == 200) {
			System.out.println("调用请求成功!");
			String result = EntityUtils.toString(response.getEntity(),"utf-8");
			System.out.println(result);
		}
	}
	

	
}
