package com.example.demoapp.database;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class MSSQL_Connector {
    private String ip;
    private String username;
    private String database;
    private String password;
    private String port;

    @SuppressLint("NewApi")
    public Connection connector() {
        ip = "192.168.85.213";
        port = "1433";
        password = "12345";
        username = "sa";
        database = "stock";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Connection con = null;
        String connection_url = null;

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connection_url = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";databaseName=" + database + ";user=" + username + ";password=" + password + ";";
            con = DriverManager.getConnection(connection_url);

        } catch (Exception e) {
            Log.d("connector:", e.toString());
        }
        return con;
    }

}
