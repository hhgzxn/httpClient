package com.atos.demo;

import org.junit.Test;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class HttpClientDemoTest {

    /*@Test
    public void mainTest() {
        System.out.println("main");
        String[] args = {"out/artifacts/httpClient_jar/coupa.properties"};
        HttpClientDemo.main(args);
        assertEquals(1, 1);
    }*/

    public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    public static final String MYSQL_URL = "jdbc:mysql://localhost/javaTestDB?"
            + "user=javauser&password=javapass";


    @Test
    public void DBTest() throws ClassNotFoundException {
        DBUtils.getConnection();

    }
}