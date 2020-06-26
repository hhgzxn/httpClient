/*
 * Copyright (c) 2020, Atos Information Technology HK Limited. All rights reserved.
 * ATOS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package com.atos.demo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName HttpUtils
 * @Description HTTP Request Handler
 * @Author Tony.Zhang
 * @Date 6/23/2020 2:26 PM
 **/
public class HttpUtils {

    /**
     * @methodsName: doGet
     * @description: GET
     * @param url
     * @param getList
     * @param headerParamMaps
     * @return java.lang.String
     * @author Tony.Zhang
     * @date 6/23/2020 2:49 PM
     */
    public static String doGet(String url,
                               List<String> getList,
                               Map<String, String> headerParamMaps,
                               String proxyHost,
                               int proxyPort) {
        // create a http client.
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        // GET params
        StringBuffer params = new StringBuffer();
        for (String v : getList) {
            params.append(v + "&");
        }

        // Create GET Request
        HttpGet httpGet = new HttpGet(url + "?" + params);
        for(String k : headerParamMaps.keySet()) {
            httpGet.setHeader(k, headerParamMaps.get(k));
        }
        // Response to Model
        CloseableHttpResponse response = null;
        String responseStr = "";
        try {
            // config info
            RequestConfig requestConfig = RequestConfig.custom()
                    // Set connection timeout (in milliseconds)
                    .setConnectTimeout(5000)
                    // Set request timeout (in milliseconds)
                    .setConnectionRequestTimeout(5000)
                    // Set socket read & write timeout (in milliseconds)
                    .setSocketTimeout(5000)
                    // Set Proxy
                    .setProxy(new HttpHost(proxyHost, proxyPort))
                    // Set whether redirection is allowed (default is true)
                    .setRedirectsEnabled(true).build();

            // Apply the above configuration information to this GET request
            httpGet.setConfig(requestConfig);

            // GET request executed (sent) by client
            response = httpClient.execute(httpGet);

            // Get response object from response model
            HttpEntity responseEntity = response.getEntity();

            System.out.println("Response Status:" + response.getStatusLine());
            if (responseEntity != null) {
                System.out.println("Response Content Length:" + responseEntity.getContentLength());
                responseStr = EntityUtils.toString(responseEntity);
                String responseStrBeautified = JSONArray.toJSONString(JSONArray.parseArray(responseStr), true);
                System.out.println("Response Content:" + responseStrBeautified);
            }
            return responseStr;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // release resources
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return responseStr;
        }
    }

    /**
     * @methodsName: doPost
     * @description: POST
     * @param
     * @return void
     * @author Tony.Zhang
     * @date 6/23/2020 2:48 PM
     */
    public void doPost() {

        // create a http client.
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        // create a  POST request
        HttpPost httpPost = new HttpPost("http://localhost:8080/testPost");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", " ");
        jsonObject.put("age", 18);
        jsonObject.put("gender", " ");
        jsonObject.put("motto", " ");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("user", jsonObject);

        // Use Alibaba's fastjson to convert an object into a JSON string;
        // need to import com.alibaba.fastjson.json package)
        String jsonString = JSONObject.toJSONString(param);

        StringEntity entity = new StringEntity(jsonString, "UTF-8");

        // For POST requests, parameters will be put in the request body for passing;
        // here, we put the entity in the post request body
        httpPost.setEntity(entity);

        httpPost.setHeader("Content-Type", "application/json;charset=utf8");

        // Response model
        CloseableHttpResponse response = null;
        try {
            // POST request executed (sent) by client
            response = httpClient.execute(httpPost);
            // Get response object from response model
            HttpEntity responseEntity = response.getEntity();

            System.out.println("Response Status:" + response.getStatusLine());
            if (responseEntity != null) {
                System.out.println("Response Content Length:" + responseEntity.getContentLength());
                System.out.println("Response Content:" + EntityUtils.toString(responseEntity));
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // release resources
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @methodsName: writeToFile
     * @description: write data to file specified.
     * @param filename
     * @param content
     * @return void
     * @author Tony.Zhang
     * @date 6/26/2020 2:23 PM
     */
    public static void writeToFile(String filename, String content) {
        try {
            File myObj = new File(filename);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
            FileWriter myWriter = new FileWriter(filename);
            myWriter.write(content);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
