package com.atos.demo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @ClassName: HttpClientDemo
 * @Description:
 * @author: Tony.Zhang
 * @create: 2020/6/22 1:07
 **/
public class HttpClientDemo {

    public static void main(String[] args) {
        String propName = args[0];
        List<String> httpGETList = new ArrayList<String>();
        Map<String, String> httpHeaderMaps = new HashMap<String, String>();

        try {
            // load .properties file.
            Properties props = new Properties();
            props.load(new FileInputStream(propName));

            // iterate through Properties.
            // assign values to Header & GET Maps separately.
            Enumeration<String> enums = (Enumeration<String>) props.propertyNames();
            while (enums.hasMoreElements()) {
                String k = enums.nextElement();
                if (k.contains("header_param")) {
                    String[] str = props.getProperty(k).split("&");
                    httpHeaderMaps.put(str[0], str[1]);
                } else if (k.contains("get_param")) {
                    httpGETList.add(props.getProperty(k));
                }
            }

            // send GET request to get JSON from remote url.
            String responseStr = HttpUtils.doGet(
                    props.getProperty("url"),
                    httpGETList,
                    httpHeaderMaps,
                    props.getProperty("proxy_hostname"),
                    Integer.parseInt(props.getProperty("proxy_port")));

            // insert json data to DB table.
            DBUtils.init(props.getProperty("conn_url"),
                    props.getProperty("conn_username"),
                    props.getProperty("conn_password"));
            DBUtils.insertDB(responseStr);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
