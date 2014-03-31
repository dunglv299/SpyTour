package com.teusoft.spytour.util;

import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.*;
import java.net.SocketTimeoutException;
import java.util.List;

/**
 * Created by DungLV on 28/3/2014.
 */
public class HttpRequest {


    public static String makeHttpRequest(String url, String method,
                                         List<NameValuePair> params) throws Exception {
        InputStream inputStream = null;
        String jsonResult = "";

        // Making HTTP request
        try {

            // check for request method
            if (method == "POST") {
                // request method is POST
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));

                // new
                HttpParams httpParameters = httpPost.getParams();
                // Set the timeout in milliseconds until a connection is
                // established.
                int timeoutConnection = 10000;
                HttpConnectionParams.setConnectionTimeout(httpParameters,
                        timeoutConnection);
                // Set the default socket timeout (SO_TIMEOUT)
                // in milliseconds which is the timeout for waiting for data.
                int timeoutSocket = 10000;
                HttpConnectionParams
                        .setSoTimeout(httpParameters, timeoutSocket);
                // new
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                inputStream = httpEntity.getContent();

            } else if (method == "GET") {
                // request method is GET
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;
                HttpGet httpGet = new HttpGet(url);
                // new
                HttpParams httpParameters = httpGet.getParams();
                // Set the timeout in milliseconds until a connection is
                // established.
                int timeoutConnection = 10000;
                HttpConnectionParams.setConnectionTimeout(httpParameters,
                        timeoutConnection);
                // Set the default socket timeout (SO_TIMEOUT)
                // in milliseconds which is the timeout for waiting for data.
                int timeoutSocket = 10000;
                HttpConnectionParams
                        .setSoTimeout(httpParameters, timeoutSocket);
                // new
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                inputStream = httpEntity.getContent();
            }

        } catch (UnsupportedEncodingException e) {
            throw new Exception("Unsupported encoding error.");
        } catch (ClientProtocolException e) {
            throw new Exception("Client protocol error.");
        } catch (SocketTimeoutException e) {
            throw new Exception("Sorry, socket timeout.");
        } catch (ConnectTimeoutException e) {
            throw new Exception("Sorry, connection timeout.");
        } catch (IOException e) {
            throw new Exception("I/O error(May be server down).");
        }
        try {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(
//                    inputStream, "iso-8859-1"), 8);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
//            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            inputStream.close();
            jsonResult = sb.toString();
            Log.d("Read from server", jsonResult);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return jsonResult;
    }


    public static String makeGetHttpRequest(String url, String paramString)
            throws Exception {
        InputStream is = null;
        String jsonResult = "";
        try {
            // request method is GET
            DefaultHttpClient httpClient = new DefaultHttpClient();
            url += paramString;
            HttpGet httpGet = new HttpGet(url);
            // new
            HttpParams httpParameters = httpGet.getParams();
            // Set the timeout in milliseconds until a connection is
            // established.
            int timeoutConnection = 10000;
            HttpConnectionParams.setConnectionTimeout(httpParameters,
                    timeoutConnection);
            // Set the default socket timeout (SO_TIMEOUT)
            // in milliseconds which is the timeout for waiting for data.
            int timeoutSocket = 10000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            // new
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
        } catch (UnsupportedEncodingException e) {
            throw new Exception("Unsupported encoding error.");
        } catch (ClientProtocolException e) {
            throw new Exception("Client protocol error.");
        } catch (SocketTimeoutException e) {
            throw new Exception("Sorry, socket timeout.");
        } catch (ConnectTimeoutException e) {
            throw new Exception("Sorry, connection timeout.");
        } catch (IOException e) {
            throw new Exception("I/O error(May be server down).");
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);

            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            jsonResult = sb.toString();
            Log.d("Read from server", jsonResult);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        // return JSON String
        return jsonResult;
    }
}
