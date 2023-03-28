package shs.sc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.widget.TextView;
import android.widget.Toast;

public class DataReceiver {
	
	public static void setInfoOnTextView(final TextView v, final String sport, final String league, final String date){
		Thread newThread = new Thread(new Runnable(){

			@Override
			public void run() {
				
				final String message = getHTML(/*URL GOES HERE!!!*/"http://73.162.233.8:8080/requestdata.jsp?sport="+sport+"&league="+league+"&date="+date).replaceAll("%", "\n");

				v.post(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						v.setText(message);
						MainActivity.scrollToBottom();
						//this is what causes the ScrollView to scroll automatically to the bottom once GO is pressed
					}
					
				});
				
			}
			
		});
		newThread.start();
	}
	
	private static String getHTML(String url){
		//this thing is probably really hard to read. Don't be too concerned about it.
		
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		HttpResponse response = null;
		try {
			response = client.execute(request);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String html = "";
		InputStream in = null;
		try {
			in = response.getEntity().getContent();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder str = new StringBuilder();
		String line = null;
		try {
			while((line = reader.readLine()) != null)
			{
			    str.append(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		html = str.toString();
		return html;
	}

}

--------------------

package com.yc.utils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
/**
 * @since2014.12.13
 * @author chenshuwan
 *
 */
public class HttpUtil {
	private static AsyncHttpClient client = new AsyncHttpClient(); // 实例话对象
	static {
		client.setTimeout(11000); // 设置链接超时，如果不设置，默认为10s
	}

	public static void get(String urlString, AsyncHttpResponseHandler res) // 用一个完整url获取一个string对象
	{
		client.get(urlString, res);
	}

	public static void get(String urlString, RequestParams params, AsyncHttpResponseHandler res) // url里面带参数
	{
		client.get(urlString, params, res);
	}

	public static void get(String urlString, JsonHttpResponseHandler res) // 不带参数，获取json对象或者数组
	{
		client.get(urlString, res);
	}

	public static void get(String urlString, RequestParams params, JsonHttpResponseHandler res) // 带参数，获取json对象或者数组
	{
		client.get(urlString, params, res);
	}

	public static void get(String uString, BinaryHttpResponseHandler bHandler) // 下载数据使用，会返回byte数据
	{
		client.get(uString, bHandler);
	}

	public static AsyncHttpClient getClient() {
		return client;
	}
}

--------------------

package io.getstream.core.utils;

import io.getstream.core.http.HTTPClient;
import io.getstream.core.http.Request.Builder;
import io.getstream.core.http.Token;
import io.getstream.core.options.RequestOption;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public final class Request {
  private Request() {
    /* nothing to see here */
  }

  public static Builder buildRequest(URL url, String apiKey, Token token, RequestOption... options)
      throws URISyntaxException, MalformedURLException {
    final Builder builder =
        HTTPClient.requestBuilder().url(url).token(token).addQueryParameter("api_key", apiKey);
    for (RequestOption option : options) {
      option.apply(builder);
    }
    return builder;
  }

  public static io.getstream.core.http.Request buildGet(
      URL url, String apiKey, Token token, RequestOption... options)
      throws URISyntaxException, MalformedURLException {
    return buildRequest(url, apiKey, token, options).get().build();
  }

  public static io.getstream.core.http.Request buildDelete(
      URL url, String apiKey, Token token, RequestOption... options)
      throws URISyntaxException, MalformedURLException {
    return buildRequest(url, apiKey, token, options).delete().build();
  }

  public static io.getstream.core.http.Request buildPost(
      URL url, String apiKey, Token token, byte[] payload, RequestOption... options)
      throws URISyntaxException, MalformedURLException {
    return buildRequest(url, apiKey, token, options).post(payload).build();
  }

  public static io.getstream.core.http.Request buildMultiPartPost(
      URL url,
      String apiKey,
      Token token,
      String fileName,
      byte[] payload,
      RequestOption... options)
      throws URISyntaxException, MalformedURLException {
    return buildRequest(url, apiKey, token, options).multiPartPost(fileName, payload).build();
  }

  public static io.getstream.core.http.Request buildMultiPartPost(
      URL url, String apiKey, Token token, File payload, RequestOption... options)
      throws URISyntaxException, MalformedURLException {
    return buildRequest(url, apiKey, token, options).multiPartPost(payload).build();
  }

  public static io.getstream.core.http.Request buildPut(
      URL url, String apiKey, Token token, byte[] payload, RequestOption... options)
      throws URISyntaxException, MalformedURLException {
    return buildRequest(url, apiKey, token, options).put(payload).build();
  }
}

--------------------

