package com.example.palhunter;

import android.os.Handler;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class DatabaseClient {
  private static final String BASE_URL = "http://hamedaan.usc.edu:8080/team17/QueryServlet?";

  private static AsyncHttpClient client = new AsyncHttpClient();

  public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
	  System.out.println("get request: " + getAbsoluteUrl(url));
	  if(params == null)
		  client.get(getAbsoluteUrl(url), responseHandler);
	  else
		  client.get(getAbsoluteUrl(url), params, responseHandler);
  }

  public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
      client.post(getAbsoluteUrl(url), params, responseHandler);
  }

  private static String getAbsoluteUrl(String relativeUrl) {
      return BASE_URL + relativeUrl;
  }
}


