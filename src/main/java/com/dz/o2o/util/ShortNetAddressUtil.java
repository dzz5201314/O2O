package com.dz.o2o.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

//需要在pom.xml引入以下依赖
/**
<!-- https://mvnrepository.com/artifact/com.alibaba/fastjson -->
<dependency>
  <groupId>com.alibaba</groupId>
  <artifactId>fastjson</artifactId>
  <version>1.2.45</version>
</dependency>
<!-- https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp -->
<dependency>
  <groupId>com.squareup.okhttp3</groupId>
  <artifactId>okhttp</artifactId>
  <version>3.9.1</version>
</dependency>
**/
/**
 * 
 * 使用新浪的短链接服务生成短链接
 * 
 */

public class ShortNetAddressUtil {

	static String actionUrl = "https://api.xiaomark.com/v1/link/create";

	static String APIKEY = "a04e23bf4096335ae5c2c66032a7a61f";

	public static void main(String[] args) {
		String longUrl = "https://mp.weixin.qq.com/debug/cgi-bin/sandbox?t=sandbox/login";
		System.out.println(getShortURL(longUrl));

	}

	@SuppressWarnings("deprecation")
	public static String getShortURL(String longUrl) {
		// longUrl = java.net.URLEncoder.encode(longUrl);
		String apikey = APIKEY;
		JSONObject param = new JSONObject();
		param.put("apikey", apikey);
		param.put("origin_url", longUrl);

		// 利用RestTemplate进行第三方接口的调用
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		// 设置超时时间30s
		requestFactory.setConnectTimeout(30 * 1000);
		requestFactory.setReadTimeout(30 * 1000);
		RestTemplate restTemplate = new RestTemplate(requestFactory);
		ResponseEntity<JSONObject> jsonObjectResponseEntity = restTemplate.postForEntity(actionUrl, param,
				JSONObject.class);
		// 将得到的json对象的body部分进行处理
		JSONObject json = jsonObjectResponseEntity.getBody();
		if (json == null) {
			return "";
		}
		// 读取多层嵌套的json数据
		return json.getJSONObject("data").getJSONObject("link").getString("url");
	}

	public static String sinaShortUrl(String source, String longUrl) {
		String result = sendPost(actionUrl, "url_long=" + longUrl + "&source=" + source);
		if (result == null || "".equals(result)) {
			return "";
		}
		JSONArray jsonArr = JSON.parseArray(result);
		JSONObject json = JSON.parseObject(jsonArr.get(0).toString());
		String ret = json.get("url_short").toString();
		return ret;
	}

	/**
	 * 
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url   发送请求的 URL
	 * 
	 * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * 
	 * @return 所代表远程资源的响应结果
	 * 
	 * 
	 * 
	 */

	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

}
