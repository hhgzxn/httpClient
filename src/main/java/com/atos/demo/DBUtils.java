/*
 * Copyright (c) 2020, Atos Information Technology HK Limited. All rights reserved.
 * ATOS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package com.atos.demo;

import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.driver.OracleConnection;
import oracle.sql.CLOB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName DBUtils
 * @Description TODO
 * @Author Tony.Zhang
 * @Date 6/24/2020 4:54 PM
 **/
public class DBUtils {

    private static String url;
    private static String username;
    private static String password;

    public static void init(String url, String username, String password) {
        DBUtils.url = url;
        DBUtils.username = username;
        DBUtils.password = password;
    }

    /**
     * @param
     * @return java.sql.Connection
     * @methodsName: getConnection
     * @description: Get DB connection
     * @author Tony.Zhang
     * @date 6/24/2020 6:00 PM
     */
    public static Connection getConnection() {
        System.out.println("Oracle JDBC Connection Testing ~");
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        List<Map<String, String>> result = new ArrayList<Map<String, String>>();

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
        return conn;
    }

    /**
     * @param content
     * @return void
     * @methodsName: insertDB
     * @description: Insert content to DB
     * @author Tony.Zhang
     * @date 6/24/2020 5:55 PM
     */
    public static void insertDB(String content) {

        String SQL_INSERT = "INSERT INTO pru_coupa_intf_json VALUES (coupa_seq.NEXTVAL, 'SIM', sysdate, SYSTIMESTAMP, -1, ?)";
        try {
            Connection conn = getConnection();
            conn.setAutoCommit(false);

            // String to Clob
            CLOB clob = new CLOB((OracleConnection)conn);
            clob = oracle.sql.CLOB.createTemporary((OracleConnection)conn, true, 1);
            clob.putString(1, content);

            // execute SQL statement
            OraclePreparedStatement ops = (OraclePreparedStatement)conn.prepareCall(SQL_INSERT);
            ops.setCLOB(1, clob);
            ops.executeUpdate();

            // close connection
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}